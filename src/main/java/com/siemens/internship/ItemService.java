package com.siemens.internship;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Changed the type of id from Long to UUID.
 * Changed the processing status from hard-coded string to enumeration ItemStatus.
 * <p>
 * Added @RequiredArgsConstructor annotation to get rid of boilerplate and perform a constructor injection instead
 * of field injection.
 * Removed @Autowired because Spring manages injection alone as long as we only have one constructor declared with
 * lombok for Service class
 * <p>
 * Got rid of processedItems as this class needs to be immutable:
 * BEFORE:
 * -every request made on /process API would use the same processedItems data structure
 * therefore getting mixed results and race conditions
 * -if we kept that attribute we would have to delete its contentents after every request
 * -during the same request multiple working threads could call the .add() method
 * -the data stored in it does not serve a role in any pipeline stage
 * <p>
 * NOW:
 * -every request receives its own local collection, therefore there is no shared state
 * -the items processed are not cached and the service class serves its purpose of only performing operations on data.
 * -threads never touch the same list, no race conditions
 * -the memory is better managed by the garbage collection, no useless cached data
 * <p>
 * Got rid of processedCount as is equivalent scope is obtained by calling .size() method
 * on the returned list. It is a big improvement as now there is no shared resource between 2
 * request happening at the same time on the same endpoint. The information stored in processedCount
 * could be considered redundant as it is a property of the returned list.
 * <p>
 * Removed:    private static ExecutorService executor = Executors.newFixedThreadPool(10);
 * Added:      private final ThreadPoolTaskExecutor itemExecutor;
 * <p>
 * We did this change because ThreadPoolTaskExecutor is managed by Spring as we declared a bean with the desired
 * configuration. Also, now there is only one Thread executor running for every API request, that is managed by spring.
 * ThreadPoolTaskExecutor is easy to configure and maintain.
 */
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ThreadPoolTaskExecutor itemExecutor;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(UUID id) {
        return itemRepository.findById(id);
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    public void deleteById(UUID id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        itemRepository.deleteById(id);
    }


    /**
     * Your Tasks
     * Identify all concurrency and asynchronous programming issues in the code
     * Fix the implementation to ensure:
     * All items are properly processed before the CompletableFuture completes
     * Thread safety for all shared state
     * Proper error handling and propagation
     * Efficient use of system resources
     * Correct use of Spring's @Async annotation
     * Add appropriate comments explaining your changes and why they fix the issues
     * Write a brief explanation of what was wrong with the original implementation
     *
     * Hints
     * Consider how CompletableFuture composition can help coordinate multiple async operations
     * Think about appropriate thread-safe collections
     * Examine how errors are handled and propagated
     * Consider the interaction between Spring's @Async and CompletableFuture
     */

    /**
     * The method starts execution on a new thread from the pool different from the HTTP server thread.
     * We retrieve the list of ids from the database, then we start processing the ids via java streams:
     * for every id we apply processingItemAsync and group the processed id into a list
     *
     * @return returns a list wrapped in CompletableFuture as it allows for non-blocking processing of data
     * while the asynchronous processing is still going on. It waits asynchronous processing to end then groups
     * the stream into CompletableFuture[0], joins the result and transform it to a list.
     */
    @Async("itemExecutor")
    public CompletableFuture<List<Item>> processItemsAsync() {

        List<UUID> itemIds = itemRepository.findAllIds();

        List<CompletableFuture<Item>> processedItems = itemIds.stream()
                .map(this::processSingleItemAsync)
                .toList();

        return CompletableFuture
                .allOf(processedItems.toArray(new CompletableFuture[0]))
                .thenApply(element -> processedItems.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    /**
     * In case an item is not found by id there is an EntityNotFoundException
     *
     * @param id the id of the item
     * @return The truly asynchronous implementation of the item processing. Every item change of status is here
     * running on a different thread, running independently of the others. The threads are pooled from itemExecutor
     * bean.
     */
    @Transactional
    public CompletableFuture<Item> processSingleItemAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item not found"));
            item.setStatus(ItemStatus.PROCESSED);
            return itemRepository.save(item);
        }, itemExecutor);
    }

}


package com.siemens.internship;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    /**
     * I changed the id field from integer type to UUID as it provides a better random unique
     * identifier for the users, rather than numeric Long
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Added extra validation constrains for the other fields of the item, such as:
     *      -minimum/maximum string size
     *      -not blank requirement
     * This enhances the security of our application.
     */
    @NotBlank(message = "You must insert a name")
    @Size(max = 124, min = 2)
    private String name;

    @NotBlank(message = "You must insert a description")
    @Size(max = 1024, min = 8)
    private String description;


    /**
     * Here is a little improvement on the overall codebase design, as the item
     * is going through the processing pipeline I consider that we better
     * use an enumeration type to keep track of the item's pipeline stage rather than
     * using hard-coded strings that are prone to typos.
     * The annotation assures that the column "status" from our database is using
     * the String name not the Enumeration number.
     */
    @Enumerated(EnumType.STRING)
    private ItemStatus status = ItemStatus.NEW;

    /**
     * Here we check that the email address is not empty.
     * Email annotations checks if the email as the specific body of an email:
     *  -@domain
     *  -.domainn
     */
    @NotBlank(message = "You must insert an email address")
    @Email(message = "You must insert a valid email address")
    private String email;
}
package com.siemens.internship;

/**
 * Here I thought it would be a better choice to use an enumerated type for expressing the item state in the
 * processing pipeline.
 */
public enum ItemStatus {
    NEW,
    PROCESSED,
    TERMINATED,
    ERROR
}

package com.kerware.simulateurreusine;

/**
 * Generic interface for calculating values of type T.
 *
 * @param <T> the type of value to calculate
 */
interface Calculateur<T> {

    /**
     * Calculates and returns a value of type T.
     *
     * @return the calculated value
     */
    T calculer();
}

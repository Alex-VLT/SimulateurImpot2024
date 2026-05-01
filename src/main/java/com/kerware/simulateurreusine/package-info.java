/**
 * Refactored tax calculation simulator with separated concerns.
 *
 * This package contains a refactored implementation of the income tax
 * calculation simulator, designed for testability, maintainability, and
 * parameterizability. It replaces the monolithic original Simulateur class
 * with specialized calculator classes following the Strategy pattern.
 *
 * <h2>Key Components:</h2>
 * <ul>
 *   <li>SimulateurReusine: Main orchestrator coordinating calculation steps</li>
 *   <li>CalculateurAbattement: Handles income tax allowance (déduction)</li>
 *   <li>CalculateurBareme: Applies progressive tax bracket calculation</li>
 *   <li>CalculateurPartsFiscales: Computes household tax shares</li>
 *   <li>CalculateurDecote: Applies tax relief (décote) benefits</li>
 *   <li>CalculateurPlafondDemiPart: Applies half-share ceiling constraints</li>
 *   <li>ParametresImpot: Centralizes all tax parameters and constants</li>
 * </ul>
 *
 * <h2>Design Patterns:</h2>
 * <ul>
 *   <li>Strategy Pattern: Each calculator implements Calculateur interface</li>
 *   <li>Immutable Objects: Calculators receive parameters in constructors</li>
 *   <li>Separation of Concerns: Each calculator handles one responsibility</li>
 * </ul>
 *
 * @see com.kerware.simulateur.ICalculateurImpot
 * @see com.kerware.simulateurreusine.SimulateurReusine
 */
package com.kerware.simulateurreusine;

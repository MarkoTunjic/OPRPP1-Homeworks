package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * A class that implements the {@link CalcModel} and represents a calculator
 * that does not care about operator order
 *
 * @author Marko Tunjić
 */
public class CalcImpl implements CalcModel {
    /**
     * An private attribute that indicates if the current calculators value is
     * editable or not
     */
    private boolean editable;

    /**
     * An private attribute that indicates if the current calculators value is
     * positive or not
     */
    private boolean positive;

    /**
     * An private attribute that represents the current calculators value in string
     * format
     */
    private String stringInput;

    /**
     * An private attribute that represents the current calculators value in double
     * format
     */
    private double doubleInput;

    /**
     * An private attribute that represents the current calculators frozen value in
     * string format
     */
    private String frozen;

    /**
     * An private attribute that represents the first operand of a binary operation
     */
    private double activeOperand;

    /**
     * An private attribute that indicates if the first operand of a binary
     * operation is set
     */
    private boolean activeOperandSet;

    /** An private attribute that represents the current pending binary operation */
    private DoubleBinaryOperator pendingBinaryOperation;

    /**
     * An private attribute that contains all the interested listeners of this
     * calculator
     */
    private List<CalcValueListener> listeners;

    /** A deafult constructors that sets the attributes to their default value */
    public CalcImpl() {
        editable = true;
        positive = true;
        stringInput = "";
        doubleInput = 0;
        frozen = null;
        activeOperand = 0;
        activeOperandSet = false;
        pendingBinaryOperation = null;
        listeners = new ArrayList<>();
    }

    /**
     * An private attribute that inform all listeners that a value change has
     * happened
     */
    private void fireListeners() {
        for (var listener : listeners)
            listener.valueChanged(this);
    }

    /**
     * Prijava promatrača koje treba obavijestiti kada se
     * promijeni vrijednost pohranjena u kalkulatoru.
     *
     * @param l promatrač; ne smije biti <code>null</code>
     *
     * @throws NullPointerException ako je za <code>l</code> predana vrijednost
     *                              <code>null</code>
     */
    @Override
    public void addCalcValueListener(CalcValueListener l) {
        if (l == null)
            throw new NullPointerException("interested CalcValueListener can not be null");
        listeners.add(l);
    }

    /**
     * Odjava promatrača s popisa promatrača koje treba
     * obavijestiti kada se promijeni vrijednost
     * pohranjena u kalkulatoru.
     *
     * @param l promatrač; ne smije biti <code>null</code>
     *
     * @throws NullPointerException ako je za <code>l</code> predana vrijednost
     *                              <code>null</code>
     */
    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        if (l == null)
            throw new NullPointerException("Not interested listener can not be null");
        listeners.remove(l);
    }

    /**
     * Vraća trenutnu vrijednost koja je pohranjena u kalkulatoru.
     *
     * @return vrijednost pohranjena u kalkulatoru
     */
    @Override
    public double getValue() {
        return doubleInput;
    }

    /**
     * Upisuje decimalnu vrijednost u kalkulator. Vrijednost smije
     * biti i beskonačno odnosno NaN. Po upisu kalkulator
     * postaje needitabilan.
     *
     * @param value vrijednost koju treba upisati
     */
    @Override
    public void setValue(double value) {
        doubleInput = value;
        editable = false;
        if (value == Double.POSITIVE_INFINITY) {
            stringInput = "Infinity";
            fireListeners();
            return;
        }
        if (value == Double.NEGATIVE_INFINITY) {
            stringInput = "-Infinity";
            fireListeners();
            return;
        }
        if (doubleInput == 0)
            stringInput = "0";
        else
            stringInput = Double.valueOf(value).toString();
        this.frozen = null;
        fireListeners();

    }

    /**
     * Vraća informaciju je li kalkulator editabilan (drugim riječima,
     * smije li korisnik pozivati metode {@link #swapSign()},
     * {@link #insertDecimalPoint()} te {@link #insertDigit(int)}).
     *
     * @return <code>true</code> ako je model editabilan, <code>false</code> inače
     */
    @Override
    public boolean isEditable() {
        return editable;
    }

    /**
     * Resetira trenutnu vrijednost na neunesenu i vraća kalkulator u
     * editabilno stanje.
     */
    @Override
    public void clear() {
        doubleInput = 0;
        stringInput = "";
        editable = true;
        positive = true;
        fireListeners();
    }

    /**
     * Obavlja sve što i {@link #clear()}, te dodatno uklanja aktivni
     * operand i zakazanu operaciju.
     */
    @Override
    public void clearAll() {
        editable = true;
        positive = true;
        stringInput = "";
        doubleInput = 0;
        frozen = null;
        activeOperand = 0;
        activeOperandSet = false;
        pendingBinaryOperation = null;
        fireListeners();
    }

    /**
     * Mijenja predznak unesenog broja.
     *
     * @throws CalculatorInputException ako kalkulator nije editabilan
     */
    @Override
    public void swapSign() throws CalculatorInputException {
        if (!editable)
            throw new CalculatorInputException("Currently not editable!");
        positive = !positive;

        if (stringInput.length() == 0) {
            fireListeners();
            return;
        }

        StringBuilder sb = new StringBuilder(stringInput);
        if (stringInput.charAt(0) == '-')
            sb.deleteCharAt(0);
        else
            sb.insert(0, "-");
        stringInput = sb.toString();
        doubleInput = Double.parseDouble(stringInput);
        fireListeners();
    }

    /**
     * Dodaje na kraj trenutnog broja decimalnu točku.
     *
     * @throws CalculatorInputException ako nije još unesena niti jedna znamenka
     *                                  broja,
     *                                  ako broj već sadrži decimalnu točku ili ako
     *                                  kalkulator nije editabilan
     */
    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if (!editable)
            throw new CalculatorInputException("Currently not editable!");
        if (stringInput.contains("."))
            throw new CalculatorInputException("Already contains dot");
        if (stringInput.length() == 0)
            throw new CalculatorInputException("Cannot add decimal point on empty number");
        stringInput += ".";
        fireListeners();
    }

    /**
     * U broj koji se trenutno upisuje na kraj dodaje poslanu znamenku.
     * Ako je trenutni broj "0", dodavanje još jedne nule se potiho
     * ignorira.
     *
     * @param digit znamenka koju treba dodati
     * @throws CalculatorInputException ako bi dodavanjem predane znamenke broj
     *                                  postao prevelik za konačan prikaz u tipu
     *                                  {@link Double}, ili ako kalkulator nije
     *                                  editabilan.
     * @throws IllegalArgumentException ako je <code>digit &lt; 0</code> ili
     *                                  <code>digit &gt; 9</code>
     */
    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if (!editable)
            throw new CalculatorInputException("Currently not editable!");
        if (digit > 9 || digit < 0)
            throw new IllegalArgumentException("Invalid digit was given");
        if (stringInput.length() == 0 || stringInput.equals("0"))
            stringInput = String.valueOf(digit);
        else
            stringInput += digit;
        freezeValue(null);
        doubleInput = Double.parseDouble(stringInput);
        if (Double.isInfinite(doubleInput))
            throw new CalculatorInputException("Too big number");
        fireListeners();
    }

    /**
     * Provjera je li upisan aktivni operand.
     *
     * @return <code>true</code> ako je aktivani operand upisan, <code>false</code>
     *         inače
     */
    @Override
    public boolean isActiveOperandSet() {
        return activeOperandSet;
    }

    /**
     * Dohvat aktivnog operanda.
     *
     * @return aktivni operand
     *
     * @throws IllegalStateException ako aktivni operand nije postavljen
     */
    @Override
    public double getActiveOperand() throws IllegalStateException {
        if (!isActiveOperandSet())
            throw new IllegalStateException("Active operand is not set");
        return activeOperand;
    }

    /**
     * Metoda postavlja aktivni operand na predanu vrijednost.
     * Ako kalkulator već ima postavljen aktivni operand, predana
     * vrijednost ga nadjačava.
     *
     * @param activeOperand vrijednost koju treba pohraniti kao aktivni operand
     */
    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
        activeOperandSet = true;
    }

    /**
     * Uklanjanje zapisanog aktivnog operanda.
     */
    @Override
    public void clearActiveOperand() {
        activeOperand = 0;
        activeOperandSet = false;
    }

    /**
     * Dohvat zakazane operacije.
     *
     * @return zakazanu operaciju, ili <code>null</code> ako nema zakazane operacije
     */
    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        if (!isActiveOperandSet())
            throw new IllegalStateException("Active operand is not set");
        return pendingBinaryOperation;
    }

    /**
     * Postavljanje zakazane operacije. Ako zakazana operacija već
     * postoji, ovaj je poziv nadjačava predanom vrijednošću.
     *
     * @param op zakazana operacija koju treba postaviti; smije biti
     *           <code>null</code>
     */
    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        pendingBinaryOperation = op;
        editable = true;
    }

    /**
     * Vraća tekst koji treba prikazati na zaslonu kalkulatora.
     * Detaljnija specifikacija dana je u uputi za domaću zadaću.
     *
     * @return tekst za prikaz na zaslonu kalkulatora
     */
    @Override
    public String toString() {
        if (hasFrozenValue())
            return frozen;
        if (stringInput.length() == 0)
            return positive ? "0" : "-0";
        return stringInput;
    }

    /**
     * A method that freezes the current calculator value
     *
     * @param value value to be frozen
     */
    @Override
    public void freezeValue(String value) {
        this.frozen = value;
    }

    /**
     * A method that return true if the calculator has a frozen value and false
     * otherwise
     *
     * @return true if has frozen value false otherwise
     */
    @Override
    public boolean hasFrozenValue() {
        return frozen != null;
    }
}

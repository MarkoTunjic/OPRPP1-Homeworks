package hr.fer.oprpp1.hw04.db;

/**
 * A class that contains all the relevant data of a student
 *
 * @author Marko Tunjić
 */
public class StudentRecord {
    /**
     * A private attribute that represents the students ID must have 10 numbers
     * inside
     */
    private String jmbag;

    /** A private attribute that represents the students first name */
    private String firstName;

    /** A private attribute that represents the students last name */
    private String lastName;

    /**
     * A private attribute that represents the students final grade it must be
     * between 1 and 5 (inclusive)
     */
    private int finalGrade;

    public StudentRecord(String jmbag, String firstName, String lastName, int finalGrade) {
        this.jmbag = jmbag;
        this.firstName = firstName;
        this.lastName = lastName;
        this.finalGrade = finalGrade;
    }

    /**
     * A method that return the jmbag of this object
     *
     * @return the jmbag
     */
    public String getJmbag() {
        return jmbag;
    }

    /**
     * A method that return the first name of this object
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * A method that return the last name of this object
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * A method that return the final grade of this object
     *
     * @return the final grade
     */
    public int getFinalGrade() {
        return finalGrade;
    }

    /**
     * A method that returns the hash code of a student the field which is used for
     * creating this method is jmbag
     *
     * @return the hash code of this student
     */
    @Override
    public int hashCode() {
        // a prime number for better hashing
        final int prime = 31;
        int result = 1;

        // calculate the hash
        result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
        return result;
    }

    /**
     * A method that returns true if this student and the given object are equal and
     * false otherwise. They are equal if the jmbags are equal.
     *
     * @param obj the object which will be compared to this student
     *
     * @return true if this student and the given object are equal
     */
    @Override
    public boolean equals(Object obj) {
        // check if the same reference was given
        if (this == obj)
            return true;

        // check if other object is null
        if (obj == null)
            return false;

        // check if same class
        if (getClass() != obj.getClass())
            return false;

        // if same class then cast
        StudentRecord other = (StudentRecord) obj;

        // check if the given jmbag field is null
        if (jmbag == null) {
            if (other.jmbag != null)
                return false;
        }
        // compare jmbags
        else if (!jmbag.equals(other.jmbag))
            return false;
        return true;
    }

}

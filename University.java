import java.util.*;


/**
 * Represents a University with students and courses.
 * The university class will maintain an index of students and courses using Maps.
 * - The keys for the students' map is the Student's ID
 * - The keys for the courses' map is the Course's Code
 *
 * @author Nilay Das
 */
public class University {
    private Map<Integer, Student> studentBody;
    private Map<String, Course> availableCourses;
    private String universityName;
    private String universityMotto;


    public University(String universityName, String universityMotto) {
        this.universityName = universityName;
        this.universityMotto = universityMotto;
        studentBody = new HashMap<>();
        availableCourses = new HashMap<>();
    }

    /**
     * Adds a student to the university roster. Student's cannot be added twice.
     *
     * @param student the student to be added
     * @return true if the student was added, false if the student was already on the map
     */
    public boolean addStudent(Student student) {
        if (studentBody.containsKey(student.getIdNumber())) {
            return false;
        } else {
            studentBody.put(student.getIdNumber(), student);
            return true;
        }
    }

    /**
     * Gets a student from the university
     *
     * @param idNumber the student's ID.
     * @return the student object or null if not found
     */
    public Student getStudent(int idNumber) {
        if (studentBody.containsKey(idNumber)) {
            Student student = studentBody.get(idNumber);
            return student;
        } else {
            return null;
        }
    }

    /**
     * Returns a list containing all students from the university
     *
     * @return A list of all students (the list will be empty if the university is empty)
     */
    public List<Student> getStudents() {

        if (studentBody.isEmpty()) {
            return new LinkedList<>();
        } else {
            LinkedList<Student> studentList = new LinkedList<>();
            Iterator<Student> iterator = studentBody.values().iterator();
            while (iterator.hasNext()) {
                studentList.add(iterator.next());

            }
            return studentList;
        }
    }

    /**
     * Add a course to the courses offered by the university
     *
     * @param course the course object to be added
     * @return true if the course was added, false if the course was already on the university
     */
    public boolean addCourse(Course course) {
        if (availableCourses.containsValue(course)) {
            return false;
        } else {
            availableCourses.put(course.getCourseCode(), course);
            return true;
        }
    }


    /**
     * Adds a course as a pre-requisite to another course. Both courses must already exist
     * in the university's list of offered courses.
     *
     * @param courseID       the id of the course you want to add the pre-requisite to
     * @param preReqCourseID the id of the pre-requisite course
     * @return false if either the courseID or preReqCourseID are invalid, true after adding the pre-requisite
     */
    public boolean addRequisiteToCourse(String courseID, String preReqCourseID) {
        if (availableCourses.containsKey(courseID) && availableCourses.containsKey(preReqCourseID)) {
            getCourse(courseID).getPreRequisites().add(getCourse(preReqCourseID));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets a course from the university
     *
     * @param courseCode the course code.
     * @return the course object associated with the code or null if not found
     */
    public Course getCourse(String courseCode) {
        if (availableCourses.containsKey(courseCode)) {
            Course course = availableCourses.get(courseCode);
            return course;
        } else {
            return null;
        }
    }

    /**
     * Get a list containing all courses offered by the university.
     *
     * @return the list of courses offered by the university
     */
    public List<Course> getCourses() {
        if (availableCourses.isEmpty()) {
            return new ArrayList<>();
        } else {
            ArrayList<Course> courseList = new ArrayList<>();
            Iterator<Course> iterator = availableCourses.values().iterator();
            while (iterator.hasNext()) {
                courseList.add(iterator.next());
            }
            return courseList;
        }
    }

    /**
     * Enroll a student in a course if the student has already passed the pre-requisites of the course.
     * The method should add the student to the course's list of students and add the course to to the student's
     * list of current courses.
     *
     * @param studentID  the id of the student
     * @param courseCode the course code for enrollment.
     * @return false if studentID, courseCode are incorrect, false if the student does not have the pre-requisites
     * true if the student was enrolled in the course.
     */
    public boolean enrollStudentInCourse(int studentID, String courseCode) {
        //TODO: Implement this method
        if (studentBody.containsKey(studentID) && availableCourses.containsKey(courseCode)) {
            if (getStudent(studentID).getEnrolledCourses().contains(getCourse(courseCode))) {
                return true;
            } else if (getCourse(courseCode).getPreRequisites().isEmpty()) {
                getCourse(courseCode).getEnrolledStudents().add(getStudent(studentID));
                getStudent(studentID).getEnrolledCourses().add(getCourse(courseCode));
                return true;
            } else {
                if (getStudent(studentID).getPreviousCourses().isEmpty()) {
                    return false;
                }
                for (Course course : getStudent(studentID).getPreviousCourses()) {
                    if (!getCourse(courseCode).getPreRequisites().contains(course)) {
                        return false;
                    }
                }
                getCourse(courseCode).getEnrolledStudents().add(getStudent(studentID));
                getStudent(studentID).getEnrolledCourses().add(getCourse(courseCode));
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Removes a student from a course if the student is already enrolled in it.
     *
     * @param studentID  the student ID
     * @param courseCode the course code
     * @return false if studentID, courseCode are incorrect, false if the student is not enrolled in the course
     * true if the student was removed from the course.
     */
    public boolean removeStudentFromCourse(int studentID, String courseCode) {
        //TODO: Implement this method
        if (getCourse(courseCode).getEnrolledStudents().contains(getStudent(studentID))) {
            getCourse(courseCode).getEnrolledStudents().remove(getStudent(studentID));
            getStudent(studentID).getEnrolledCourses().remove(getCourse(courseCode));
            return true;
        } else {
            return false;
        }

    }


    /**
     * Removes a student from the university.
     * The student will be removed from the university index AND from the list of students of every course that the
     * student was already enrolled.
     *
     * @param studentID the id of the student to remove
     * @return false if the studentID is not in the index. True if the student was removed from the index and courses
     */
    public boolean removeStudentFromUniversity(int studentID) {
        //TODO: Implement this method
        if (!studentBody.containsKey(studentID)) {
            return false;
        } else {

            for (Course course : getStudent(studentID).getEnrolledCourses()) {
                course.getEnrolledStudents().remove(getStudent(studentID));
            }

            studentBody.remove(studentID);
            return true;
        }
    }

    /**
     * Removes a course from the university.
     * This method should remove the course from the university offered courses
     * AND from each student's current courses.
     *
     * The method must remove the course from any other courses that have it as a pre-requisite
     *
     * @param courseCode the course code
     * @return false if the course code does not match any offered course. True after the course was removed
     */
    public boolean removeCourseFromUniversity(String courseCode) {
        //TODO: Implement this method
        if (!availableCourses.containsKey(courseCode)) {
            return false;
        } else {
            Course c = availableCourses.get(courseCode);
            availableCourses.remove(courseCode);
            for (String courseID : availableCourses.keySet()) {
                Course course = availableCourses.get(courseID);
                if (course.getPreRequisites().contains(c)) {
                    course.getPreRequisites().remove(c);
                }
            }

            for (Integer studentID : studentBody.keySet()) {
                Student student = studentBody.get(studentID);
                if (student.getEnrolledCourses().contains(c)) {
                    student.getEnrolledCourses().remove(c);
                }
            }

            return true;
        }
    }


    public String getUniversityMotto() {
        return universityMotto;
    }

    public String getUniversityName() {
        return universityName;
    }

    /**
     * Add a new student using name and ID
     *
     * @param name      the student's name
     * @param studentID the student's ID between 0 and 999999
     * @return true if the student was added, false if the student was already on the university
     */
    public boolean addStudent(String name, int studentID) {
        return addStudent(new Student(name, studentID));
    }

    public boolean addCourse(String name, String courseID) {
        return addCourse(new Course(name, courseID));
    }


    @Override
    public String toString() {
        return String.format("%s (%s)\nNumber of Students: %d\nNumber of Courses %d",
                universityName, universityMotto, studentBody.size(), availableCourses.size());
    }

}

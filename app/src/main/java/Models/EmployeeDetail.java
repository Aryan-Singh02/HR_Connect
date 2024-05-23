package Models;

public class EmployeeDetail{
    public String getStatus;
    public String profilepic;
    private String name;
    private String jobPosition;
    private String department;
    private String contactDetail;
    private String userID;
    private String employeeID,firstName,lastName,password,confirmPassword,emailId;

    public EmployeeDetail(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContactDetail() {
        return contactDetail;
    }

    public void setContactDetail(String contactDetail) {
        this.contactDetail = contactDetail;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public EmployeeDetail(String employeeID, String name, String password, String confirmPassword, String emailId, String getStatus, String profilepic, String jobPosition, String department, String contactDetail, String userID) {
        this.employeeID = employeeID;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.emailId = emailId;
        this.getStatus = getStatus;
        this.profilepic = profilepic;
        this.jobPosition = jobPosition;
        this.department = department;
        this.contactDetail = contactDetail;
        this.userID = userID;
    }

    public String getGetStatus() {
        return getStatus;
    }

    public void setGetStatus(String getStatus) {
        this.getStatus = getStatus;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}

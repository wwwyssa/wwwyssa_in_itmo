package models;

public class Organization {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Integer employeesCount; //Поле не может быть null, Значение поля должно быть больше 0
    private OrganizationType type; //Поле не может быть null
    private Address officialAddress; //Поле не может быть null

    public Organization(String name, Integer employeesCount, OrganizationType type, Address officialAddress) {
        this.name = name;
        this.employeesCount = employeesCount;
        this.type = type;
        this.officialAddress = officialAddress;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getEmployeesCount() {
        return employeesCount;
    }

    public OrganizationType getType() {
        return type;
    }

    public Address getOfficialAddress() {
        return officialAddress;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employeesCount=" + employeesCount +
                ", type=" + type +
                ", officialAddress=" + officialAddress +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Organization) {
            Organization organization = (Organization) obj;
            return id.equals(organization.id) && name.equals(organization.name) && employeesCount.equals(organization.employeesCount) && type.equals(organization.type) && officialAddress.equals(organization.officialAddress);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode() + name.hashCode() + employeesCount.hashCode() + type.hashCode() + officialAddress.hashCode();
    }


}

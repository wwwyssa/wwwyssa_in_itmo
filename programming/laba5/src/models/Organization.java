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
}

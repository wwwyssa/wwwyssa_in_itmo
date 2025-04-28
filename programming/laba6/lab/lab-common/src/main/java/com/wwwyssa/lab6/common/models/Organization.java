package com.wwwyssa.lab6.common.models;

import com.wwwyssa.lab6.common.util.Validatable;
/**
 * Класс, представляющий организацию.
 */
public class Organization implements Validatable {

    private Long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    private String name; // Поле не может быть null, Строка не может быть пустой

    private Integer employeesCount; // Поле не может быть null, Значение поля должно быть больше 0

    private OrganizationType type; // Поле не может быть null

    private Address officialAddress; // Поле не может быть null

    /**
     * Конструктор для создания объекта Organization.
     * @param name имя организации, не может быть null или пустым
     * @param employeesCount количество сотрудников, не может быть null и должно быть больше 0
     * @param type тип организации, не может быть null
     * @param officialAddress официальный адрес, не может быть null
     */
    public Organization(String name, Integer employeesCount, OrganizationType type, Address officialAddress) {
        this.id = null; // id будет сгенерирован автоматически
        this.name = name;
        this.employeesCount = employeesCount;
        this.type = type;
        this.officialAddress = officialAddress;
    }

    public Organization() {}
    // Геттеры и сеттеры с аннотациями JAXB

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    /**
     * Возвращает строковое представление объекта Organization.
     * @return строковое представление объекта
     */
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

    /**
     * Проверяет, равен ли данный объект другому объекту.
     * @param obj объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Organization) {
            Organization organization = (Organization) obj;
            return id.equals(organization.id) && name.equals(organization.name) && employeesCount.equals(organization.employeesCount) && type.equals(organization.type) && officialAddress.equals(organization.officialAddress);
        }
        return false;
    }

    /**
     * Возвращает хэш-код объекта Organization.
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return id.hashCode() + name.hashCode() + employeesCount.hashCode() + type.hashCode() + officialAddress.hashCode();
    }

    /**
     * Проверяет, является ли организация допустимой.
     * @return true, если организация допустима, иначе false
     */
    @Override
    public boolean isValid() {
        if (id == null || id <= 0) {
            return false;
        }
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (employeesCount == null || employeesCount <= 0){
            return false;
        }
        if (type == null) {
            return false;
        }
        if (officialAddress == null || !officialAddress.isValid()) {
            return false;
        }
        return true;
    }
}

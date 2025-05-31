package com.wwwyssa.lab7.server.dao;


import com.wwwyssa.lab7.common.models.Organization;
import com.wwwyssa.lab7.common.models.OrganizationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name="manufacturer")
@Table(name="manufacturer", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
public class OrganizationDAO implements Serializable {


  public OrganizationDAO() {
  }

  public OrganizationDAO(Organization organization) {
    this.name = organization.getName();
    this.employeesCount = organization.getEmployeesCount();
    this.type = organization.getType();
    this.street = organization.getOfficialAddress().getStreet();
    this.x = organization.getOfficialAddress().getTown().getX();
    this.y = organization.getOfficialAddress().getTown().getY();
    this.z = organization.getOfficialAddress().getTown().getZ();
  }

  public void update(Organization organization) {
    this.name = organization.getName();
    this.employeesCount = organization.getEmployeesCount();
    this.type = organization.getType();
  }

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id", nullable=false, unique=true)
  private int id;

  @NotBlank(message = "Название организации не должно быть пустым.")
  @Column(name="name", nullable=false)
  private String name; // Поле не может быть null, Строка не может быть пустой

  @Min(value = 1L, message = "Количество сотрудников должно быть больше нуля.")
  @Column(name="employees_count", nullable=false)
  private Integer employeesCount; // Значение поля должно быть больше 0

  @Column(name="organisation_type", nullable=false)
  @Enumerated(EnumType.STRING)
  private OrganizationType type; // Поле не может быть null

  @NotBlank(message = "Улица не может быть пустой.")
  @Column(name="street")
  private String street; // Строка не может быть пустой, Поле не может быть null

  @Column(name="x", nullable=false)
  private float x;

  @Column(name="y", nullable=false)
  private int y;

  @Column(name="z", nullable=false)
  private Integer z;

  @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
  @JoinColumn(name="manufacturer_id")
  private List<ProductDAO> products = new ArrayList<ProductDAO>();

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "creator_id", nullable = false)
  private UserDAO creator;

  public int getId() {
    return id;
  }

  public void setId(int id) {
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

  public void setEmployeesCount(Integer employeesCount) {
    this.employeesCount = employeesCount;
  }

  public OrganizationType getType() {
    return type;
  }

  public void setType(OrganizationType type) {
    this.type = type;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public float getX() {
      return x;
  }
  public void setX(float x) {
      this.x = x;
  }

  public int getY() {
      return y;
  }

  public void setY(int y) {
      this.y = y;
  }

  public Integer getZ() {
      return z;
  }

  public void setZ(Integer z) {
      this.z = z;
  }

  public List<ProductDAO> getProducts() {
    return products;
  }

  public void setCreator(UserDAO creator) {
    this.creator = creator;
  }

  public void setProducts(List<ProductDAO> products) {
    this.products = products;
  }

  public UserDAO getCreator() {
    return creator;
  }
}

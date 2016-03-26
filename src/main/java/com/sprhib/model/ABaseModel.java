package com.sprhib.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.sprhib.controller.BaseController;

@MappedSuperclass
public abstract class ABaseModel {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "idgenerator")
  @GenericGenerator(name = "idgenerator", strategy = "org.hibernate.id.IncrementGenerator")
  @Column(name = BaseController.MODEL_ATTRIBUTE_ID, unique = true, nullable = false)
  private Integer id;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ABaseModel other = (ABaseModel) obj;
    if (getId() == null) {
      if (other.getId() != null)
        return false;
    } else if (!getId().equals(other.getId()))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
      return this.getClass().getSimpleName() + " [id=" + id + "]";
  }

}

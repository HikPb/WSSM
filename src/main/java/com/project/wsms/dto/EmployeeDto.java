package com.project.wsms.dto;

import com.project.wsms.model.Employee;
import com.project.wsms.model.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class EmployeeDto {
    private Integer id;
    private String username;
    private String fullname;
    private String phone;
    private Role role;

    public Employee convertToEntity(){
        Employee newItem = new Employee();
        newItem.setUsername(this.username);
        newItem.setFullname(this.fullname);
        newItem.setPhone(this.phone);
        newItem.setRole(this.role);
        return newItem;
    }

    public void convertToDto(Employee item){
        this.id = item.getId();
        this.phone = item.getPhone();
        this.fullname = item.getFullname();
        this.username = item.getUsername();
        this.role = item.getRole();
    }
}

package com.project.wsms.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.project.wsms.model.Employee;

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
    private List<String> roles;

    public Employee convertToEntity(){
        Employee newItem = new Employee();
        newItem.setUsername(this.username);
        newItem.setFullname(this.fullname);
        newItem.setPhone(this.phone);
        return newItem;
    }

    public void convertToDto(Employee item){
        this.id = item.getId();
        this.phone = item.getPhone();
        this.fullname = item.getFullname();
        this.username = item.getUsername();
        this.roles = item.getRoles().stream().map(role -> role.getName().toString()).collect(Collectors.toList());
    }
}

package com.effectivemobile.TaskManagementSystem.model;


import com.effectivemobile.TaskManagementSystem.dto.PriorityDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false, length = 32)
    @NotNull
    private String name;

    public Priority(PriorityDto priorityDto){
        this.setName(priorityDto.getName());
    }
}

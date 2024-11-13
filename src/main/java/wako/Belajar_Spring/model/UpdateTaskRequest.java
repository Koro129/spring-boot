package wako.Belajar_Spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTaskRequest {

    @JsonIgnore
    @NotBlank
    private String id;  

    @Size(max=255)
    @NotBlank
    private String name;

    private Boolean isCompleted;
}

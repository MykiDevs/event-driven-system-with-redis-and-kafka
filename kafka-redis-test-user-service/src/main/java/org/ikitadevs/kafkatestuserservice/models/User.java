package org.ikitadevs.kafkatestuserservice.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Version
    @JsonIgnore
    private Long version;

    @Id
    @SequenceGenerator(name = "user_seq_id", sequenceName = "user_seq_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_id")
    private Long id;

    @NotEmpty
    @Size(min = 1, max = 20, message = "Name must be between 1 and 20!")
    private String name;

    @Email(message = "Provide correct email syntax!")
    @Size(min = 5, max = 32, message = "Email must be between 5 and 32!")
    private String email;
    @NotEmpty
    @Size(min = 1, max = 20, message = "Last name must be between 1 and 20!")
    private String lastname;
    @NotEmpty
    private String password;



//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Item> items = new ArrayList<>();
//
//    public void addItem(Item item) {
//        items.add(item);
//    }

}

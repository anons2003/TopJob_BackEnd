package com.SWP.WebServer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "history_payment")
public class HistoryPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hid")
    private int hid;

    @Column(name = "amount")
    private float amount;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "userId",
            referencedColumnName = "uid"
    )
    private User user;

    @PrePersist
    public void prePersist() {
        this.created_at = String.valueOf(System.currentTimeMillis());
    }

}

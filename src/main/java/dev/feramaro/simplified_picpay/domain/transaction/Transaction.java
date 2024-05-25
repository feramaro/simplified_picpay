package dev.feramaro.simplified_picpay.domain.transaction;

import dev.feramaro.simplified_picpay.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "payer_id", referencedColumnName = "id")
    private User payer;

    @ManyToOne
    @JoinColumn(name = "payee_id", referencedColumnName = "id")
    private User payee;

    @Column(nullable = false)
    private Double amount;
}

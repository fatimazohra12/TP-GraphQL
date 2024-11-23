package ma.projet.graph.controllers;

import lombok.AllArgsConstructor;
import ma.projet.graph.DTO.TransactionRequest;
import ma.projet.graph.entities.Compte;
import ma.projet.graph.entities.Transaction;
import ma.projet.graph.entities.TypeTransaction;
import ma.projet.graph.repositories.CompteRepository;
import ma.projet.graph.repositories.TransactionRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class TransactionControllerGraphQL {
    private TransactionRepository transactionRepository;
    private CompteRepository compteRepository;

    @MutationMapping
    public Transaction addTransaction(@Argument TransactionRequest transactionRequest) {
        Compte compte = compteRepository.findById(transactionRequest.getCompteId())
                .orElseThrow(() -> new RuntimeException("Compte not found"));

        // Conversion de la date
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(transactionRequest.getDate());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Expected yyyy-MM-dd");
        }

        Transaction transaction = new Transaction();
        transaction.setMontant(transactionRequest.getMontant());
        transaction.setDate(date); // Utiliser la date convertie
        transaction.setType(transactionRequest.getType());
        transaction.setCompte(compte);

        transactionRepository.save(transaction);

        return transaction;
    }



    @QueryMapping
    public List<Transaction> allTransactions() {
        return transactionRepository.findAll();
    }

    @QueryMapping
    public List<Transaction> compteTransactions(@Argument Long id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte not found"));
        return transactionRepository.findByCompte(compte);
    }

    @QueryMapping
    public Map<String, Object> transactionStats() {
        long count = transactionRepository.count();
        double sumDepots = transactionRepository.sumByType(TypeTransaction.DEPOT);
        double sumRetraits = transactionRepository.sumByType(TypeTransaction.RETRAIT);
        return Map.of(
                "count", count,
                "sumDepots", sumDepots,
                "sumRetraits", sumRetraits
        );
    }

}
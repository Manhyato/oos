package ru.sspo.oos.service;

import ru.sspo.oos.model.Client;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client save(Client client);
    List<Client> getAll();
    Optional<Client> findByPhone(String phone);
}





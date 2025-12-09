package ru.sspo.oos.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sspo.oos.model.Client;
import ru.sspo.oos.repository.ClientRepository;
import ru.sspo.oos.service.ClientService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> findByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }
}



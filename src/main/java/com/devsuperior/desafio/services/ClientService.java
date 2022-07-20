package com.devsuperior.desafio.services;

import com.devsuperior.desafio.dto.ClientDTO;
import com.devsuperior.desafio.entities.Client;
import com.devsuperior.desafio.repositories.ClientRepository;
import com.devsuperior.desafio.services.exceptions.ExceptionEntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {

        Page<Client> list = clientRepository.findAll(pageRequest);

        return list
                .map(x -> this.viewBodyEntity(x));

//                .map(x -> new ClientDTO(x.getId(),
//                        x.getName(),
//                        x.getCpf(),
//                        x.getIncome(),
//                        x.getBirthDate(),
//                        x.getChildren()));
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) throws ExceptionEntityNotFound {
        Optional<Client> OptionalClient= clientRepository.findById(id);

        Client client = OptionalClient.orElseThrow(() -> new ExceptionEntityNotFound("Entity Not Found"));

        return this.viewBodyEntity(client);

    }

    @Transactional(readOnly = true)
    public ClientDTO insert(ClientDTO dto) {
        //Converter para o tipo Client
        Client entity = new Client();

        //removendo pontos e traços do cpf
        dto.setCpf(dto.getCpf().replace(".", ""));
        dto.setCpf(dto.getCpf().replace("-", ""));

        this.copyDtoToEntity(dto, entity); //método criado para não precisar ficar repetindo a mesma linha de código

        entity = clientRepository.save(entity);

        return this.viewBodyEntity(entity);
    }

    //Método para setar na base de dados pegando o DTO e convertendo para ENTIDADE
    private void copyDtoToEntity(ClientDTO dto, Client entity) {

        entity.setChildren(dto.getChildren());
        entity.setBirthDate(dto.getBirthDate());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setName(dto.getName());

    }

    //Método para visualizar o corpo da requisição
    private ClientDTO viewBodyEntity(Client entity) {
        return new ClientDTO(entity.getId(),
                entity.getName(),
                entity.getCpf(),
                entity.getIncome(),
                entity.getBirthDate(),
                entity.getChildren());
    }
}

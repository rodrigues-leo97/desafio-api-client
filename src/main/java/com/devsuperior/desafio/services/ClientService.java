package com.devsuperior.desafio.services;

import com.devsuperior.desafio.dto.ClientDTO;
import com.devsuperior.desafio.entities.Client;
import com.devsuperior.desafio.repositories.ClientRepository;
import com.devsuperior.desafio.services.exceptions.ExceptionBadRequest;
import com.devsuperior.desafio.services.exceptions.ExceptionEntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public ClientDTO insert(ClientDTO dto) throws ExceptionBadRequest {
        //Converter para o tipo Client
        Client entity = new Client();
        List<Client> listEntity = clientRepository.findAll();

        //removendo pontos e traços do cpf
        dto.setCpf(dto.getCpf().replace(".", ""));
        dto.setCpf(dto.getCpf().replace("-", ""));

        //verificando se existe cpf repetido na base
        for (Client listFilter : listEntity) {
            if(dto.getCpf().equals(listFilter.getCpf())) {
                throw new ExceptionBadRequest("CPF already existing");
            }
        }

        this.copyDtoToEntity(dto, entity); //método criado para não precisar ficar repetindo a mesma linha de código

        entity = clientRepository.save(entity);

        return this.viewBodyEntity(entity);
    }
    
    
    @Transactional(readOnly = true)
	public ClientDTO update(Long id, ClientDTO clientDTO) {
		
	try {
		// Client client = clientRepository.getOne(id); Consta que o getOne isDepracated, portanto utilizei da forma convencional
		
		Client client = clientRepository.findById(id).get(); //cria obj provisório para não acessar desnecessariamente a base de dados
		
		this.copyDtoToEntity(clientDTO, client);
		client = clientRepository.save(client);
		
		return this.viewBodyEntity(client);
		
		} catch (ExceptionEntityNotFound e) {
			throw new ExceptionEntityNotFound("id not found");
		}
	}

    public void delete(Long id) {
    	try {
    		clientRepository.deleteById(id);
    	} catch (EmptyResultDataAccessException e) {
    		throw new ExceptionEntityNotFound("Id not found " + id);
		} 	
    	
    }

    //Busca por cpf
    @Transactional(readOnly = true)
    public Object findByCpf(String cpf) throws ExceptionEntityNotFound {
            List<Client> list = clientRepository.findAll();
            Client client = new Client();
            for (Client listFilter : list) {
                if (cpf.equals(listFilter.getCpf())) {
                    client = listFilter;
                    return this.viewBodyEntity(client);
                }
            }
            return "teste";
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

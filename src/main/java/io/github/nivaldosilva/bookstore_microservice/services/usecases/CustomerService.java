package io.github.nivaldosilva.bookstore_microservice.services.usecases;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.nivaldosilva.bookstore_microservice.repositories.CustomerRepository;
import io.github.nivaldosilva.bookstore_microservice.entities.Customer;
import io.github.nivaldosilva.bookstore_microservice.dtos.request.CustomerRequest;
import io.github.nivaldosilva.bookstore_microservice.dtos.response.CustomerResponse;
import org.springframework.util.StringUtils;
import io.github.nivaldosilva.bookstore_microservice.exceptions.CustomerNotFoundException;
import io.github.nivaldosilva.bookstore_microservice.exceptions.EmailAlreadyExistsException;
import io.github.nivaldosilva.bookstore_microservice.mappers.CustomerMapper;
import io.github.nivaldosilva.bookstore_microservice.services.interfaces.ICustomerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CustomerResponse registerCustomer(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }

        String encryptedPassword = passwordEncoder.encode(request.password());

        Customer customer = Customer.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(encryptedPassword)
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);
        return CustomerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(UUID id, CustomerRequest request) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);

        if (!existingCustomer.getEmail().equals(request.email()) && customerRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }

        existingCustomer.setFullName(request.fullName());
        existingCustomer.setEmail(request.email());

        if (StringUtils.hasText(request.password())) {
            existingCustomer.setPassword(passwordEncoder.encode(request.password()));
        }

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return CustomerMapper.toResponse(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException();
        }
        customerRepository.deleteById(id);
    }

}

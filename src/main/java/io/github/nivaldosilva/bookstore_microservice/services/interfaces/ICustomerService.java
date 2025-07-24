package io.github.nivaldosilva.bookstore_microservice.services.interfaces;

import java.util.List;
import java.util.UUID;
import io.github.nivaldosilva.bookstore_microservice.dtos.request.CustomerRequest;
import io.github.nivaldosilva.bookstore_microservice.dtos.response.CustomerResponse;

public interface ICustomerService {

    CustomerResponse registerCustomer(CustomerRequest request);

    CustomerResponse findCustomerById(UUID id);

    List<CustomerResponse> findAllCustomers();

    CustomerResponse updateCustomer(UUID id, CustomerRequest request);

    void deleteCustomer(UUID id);
}

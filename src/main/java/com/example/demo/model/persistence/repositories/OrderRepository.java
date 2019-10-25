package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}

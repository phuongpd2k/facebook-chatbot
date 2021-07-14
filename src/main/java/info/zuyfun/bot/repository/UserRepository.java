package info.zuyfun.bot.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import info.zuyfun.bot.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, BigDecimal> {
}

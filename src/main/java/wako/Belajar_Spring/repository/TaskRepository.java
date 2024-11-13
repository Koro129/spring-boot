package wako.Belajar_Spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import wako.Belajar_Spring.entity.Task;
import wako.Belajar_Spring.entity.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findAllByUser(User user);

    Optional<Task> findByIdAndUser(String id, User user);
}

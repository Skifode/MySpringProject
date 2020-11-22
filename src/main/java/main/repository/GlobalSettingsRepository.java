package main.repository;

import main.model.GlobalSettings;
import org.springframework.data.repository.CrudRepository;

public interface GlobalSettingsRepository extends CrudRepository<GlobalSettings, Integer> {
  GlobalSettings getById(Integer id);
}

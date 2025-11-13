package dz.handy;

import dz.handy.entity.Worker;
import dz.handy.entity.Client;
import dz.handy.repository.ServiceCategoryRepository;
import dz.handy.repository.UserRepository;
import dz.handy.repository.WorkerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
@EnableCaching
public class HandydzApplication {

	public static void main(String[] args) {
		SpringApplication.run(HandydzApplication.class, args);
	}

	@Bean
	CommandLineRunner seedRoles(dz.handy.repository.RoleRepository roleRepository, UserRepository  userRepository) {
		return args -> {
			if (!roleRepository.existsById("PROVIDER")) {
				dz.handy.entity.Role provider = new dz.handy.entity.Role();
				provider.setRoleId("PROVIDER");
				provider.setRoleDescription("Provider");
				roleRepository.save(provider);
			}

			if (!roleRepository.existsById("CLIENT")) {
				dz.handy.entity.Role client = new dz.handy.entity.Role();
				client.setRoleId("CLIENT");
				client.setRoleDescription("Client");
				roleRepository.save(client);
			}
			System.out.println("--> " + userRepository.findAll().size() + " users");

		};
	}

	@Bean
	CommandLineRunner seedCategories(dz.handy.repository.ServiceCategoryRepository categoryRepository) {
		return args -> {
			if (!categoryRepository.existsByName("Plumbing")) {
				dz.handy.entity.ServiceCategory c1 = dz.handy.entity.ServiceCategory.builder()
						.name("Plumbing")
						.description("All plumbing related services")
						.build();
				categoryRepository.save(c1);
			}
			if (!categoryRepository.existsByName("Electrical")) {
				dz.handy.entity.ServiceCategory c2 = dz.handy.entity.ServiceCategory.builder()
						.name("Electrical")
						.description("Electrical installation and repair")
						.build();
				categoryRepository.save(c2);
			}
			if (!categoryRepository.existsByName("Carpentry")) {
				dz.handy.entity.ServiceCategory c3 = dz.handy.entity.ServiceCategory.builder()
						.name("Carpentry")
						.description("Woodwork and furniture services")
						.build();
				categoryRepository.save(c3);
			}
			System.out.println("--> Seeded categories: " + categoryRepository.findAll().size());
		};
	}

	@Bean
	CommandLineRunner seedWorkers(WorkerRepository workerRepository, ServiceCategoryRepository serviceCategoryRepository) {
		return args -> {
			if (!workerRepository.existsById("worker1")) {
				Worker w1 = new Worker();
				w1.setUsername("worker1");
				w1.setEmail("worker1@example.com");
				w1.setFirstName("Ali");
				w1.setLastName("Brahimi");
				w1.setEnabled(1);
				w1.setCreationDate(new Date());
				w1.setPhoneNumber1("0550-000-001");
				w1.setSpecialization("Plumber");
				w1.setRating(4.5);
				w1.setAvailable(true);
				workerRepository.save(w1);
			}

			if (!workerRepository.existsById("worker2")) {
				Worker w2 = new Worker();
				w2.setUsername("worker2");
				w2.setEmail("worker2@example.com");
				w2.setFirstName("Nada");
				w2.setLastName("Kaci");
				w2.setEnabled(1);
				w2.setCreationDate(new Date());
				w2.setPhoneNumber1("0550-000-002");
				w2.setSpecialization("Electrician");
				w2.setRating(4.2);
				w2.setAvailable(true);
				workerRepository.save(w2);
			}

			if (!workerRepository.existsById("worker3")) {
				Worker w3 = new Worker();
				w3.setUsername("worker3");
				w3.setEmail("worker3@example.com");
				w3.setFirstName("Sami");
				w3.setLastName("Meziani");
				w3.setEnabled(1);
				w3.setCreationDate(new Date());
				w3.setPhoneNumber1("0550-000-003");
				w3.setSpecialization("Carpenter");
				w3.setRating(4.8);
				w3.setAvailable(true);
				workerRepository.save(w3);
			}
			if (!workerRepository.existsById("worker4")) {
				Worker w4 = new Worker();
				w4.setUsername("worker4");
				w4.setEmail("worker4@example.com");
				w4.setFirstName("Sami");
				w4.setLastName("Meziani");
				w4.setEnabled(1);
				w4.setCreationDate(new Date());
				w4.setPhoneNumber1("0550-000-003");
				w4.setSpecialization("Carpenter");
				w4.setRating(4.8);
				w4.setAvailable(true);
				w4.setCategory(serviceCategoryRepository.findById(Long.valueOf(1)).get());
				w4.setLatitude(36.6699702);
				w4.setLongitude(3.0570648);

				workerRepository.save(w4);
			}
			if (!workerRepository.existsById("worker5")) {
				Worker w5 = new Worker();
				w5.setUsername("worker5");
				w5.setEmail("worker5@example.com");
				w5.setFirstName("Sami");
				w5.setLastName("Meziani");
				w5.setEnabled(1);
				w5.setCreationDate(new Date());
				w5.setPhoneNumber1("0550-000-003");
				w5.setSpecialization("Carpenter");
				w5.setRating(4.8);
				w5.setAvailable(true);
				w5.setCategory(serviceCategoryRepository.findById(Long.valueOf(1)).get());
				w5.setLatitude(35.831852			);
				w5.setLongitude(5.232194);

				workerRepository.save(w5);
			}

			System.out.println("--> " + workerRepository.findAll().size() + " workers");
		};
	}

	@Bean
	CommandLineRunner seedClients(UserRepository userRepository) {
		return args -> {
			if (!userRepository.existsById("client1")) {
				Client c1 = Client.builder().build();
				c1.setUsername("client1");
				c1.setEmail("client1@example.com");
				c1.setFirstName("Amine");
				c1.setLastName("Belkacem");
				c1.setEnabled(1);
				c1.setCreationDate(new Date());
				c1.setPhoneNumber1("0661-000-001");
				userRepository.save(c1);
			}

			if (!userRepository.existsById("client2")) {
				Client c2 = Client.builder().build();
				c2.setUsername("client2");
				c2.setEmail("client2@example.com");
				c2.setFirstName("Sara");
				c2.setLastName("Haddad");
				c2.setEnabled(1);
				c2.setCreationDate(new Date());
				c2.setPhoneNumber1("0661-000-002");
				userRepository.save(c2);
			}

			if (!userRepository.existsById("client3")) {
				Client c3 = Client.builder().build();
				c3.setUsername("client3");
				c3.setEmail("client3@example.com");
				c3.setFirstName("Yacine");
				c3.setLastName("Zerrouki");
				c3.setEnabled(1);
				c3.setCreationDate(new Date());
				c3.setPhoneNumber1("0661-000-003");
				userRepository.save(c3);
			}

			if (!userRepository.existsById("client4")) {
				Client c4 = Client.builder().build();
				c4.setUsername("client4");
				c4.setEmail("client4@example.com");
				c4.setFirstName("Yacine");
				c4.setLastName("Zerrouki");
				c4.setEnabled(1);
				c4.setCreationDate(new Date());
				c4.setPhoneNumber1("0661-000-003");
				c4.setLatitude(36.652520);
				c4.setLongitude(3.091661);
				userRepository.save(c4);
			}

			System.out.println("--> Seeded clients. Total users: " + userRepository.findAll().size());
		};
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}

package dz.khidma.express;

import dz.khidma.express.entity.Role;
import dz.khidma.express.entity.Category;
import dz.khidma.express.repository.RoleRepository;
import dz.khidma.express.repository.ServiceCategoryRepository;
import dz.khidma.express.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class KhidmaExpressApplication {

	public static void main(String[] args) {
		SpringApplication.run(KhidmaExpressApplication.class, args);
	}

	@Bean
	CommandLineRunner seedRoles(RoleRepository roleRepository, UserRepository  userRepository) {
		return args -> {
			if (!roleRepository.existsById("PROVIDER")) {
				Role provider = new Role();
				provider.setRoleId("PROVIDER");
				provider.setRoleDescription("Provider");
				roleRepository.save(provider);
			}

			if (!roleRepository.existsById("CLIENT")) {
				Role client = new Role();
				client.setRoleId("CLIENT");
				client.setRoleDescription("Client");
				roleRepository.save(client);
			}
			System.out.println("--> " + userRepository.findAll().size() + " users");

		};
	}

	@Bean
	CommandLineRunner seedCategories(ServiceCategoryRepository categoryRepository) {
		return args -> {
			String[] additionalCategories = new String[] {
				"Plumber",
				"Electrician",
				"Carpenter",
				"Blacksmith",
				"HVAC Technician",
				"Gas Technician",
				"Water Heater Repair",
				"Door and Window Repair",
				"Construction Worker",
				"Tile Installer",
				"Gypsum Board Installer/Decorator",
				"Painter",
				"Thermal and Waterproofing Insulation",
				"Tile and Ceramic Installation",
				"Demolition and Renovation",
				"House Cleaning",
				"Post-Construction Apartment Cleaning",
				"Carpet and Rug Cleaning",
				"Water Tank Cleaning",
				"Garden Cleaning",
				"Disinfection and Pest Control",
				"Car Mechanic",
				"Car Electrician",
				"Oil Change",
				"Mobile Car Wash",
				"Tire Repair",
				"Mobile Technical Inspection",
				"Goods and Furniture Transport",
				"Truck with Driver",
				"Furniture Lifter",
				"Local Delivery",
				"Computer Maintenance",
				"WiFi Network Installation",
				"Phone Repair",
				"Security Camera Installation",
				"Website Development",
				"Printer Maintenance",
				"Gardener",
				"Lawn Mower",
				"Garden Designer",
				"Irrigation System Installation",
				"Tree Pruning",
				"Tailor",
				"Photographer",
				"Interior Designer",
				"Custom Furniture Maker Request",
				"Artistic Blacksmith",
				"Engraver",
				"Public Writer",
				"Printing and Photocopying",
				"Translation",
				"Document Extraction",
				"Caterer",
				"Traditional Pastry Chef",
				"Chair and Table Rental",
				"DJ and Musician",
				"Nanny",
				"Home Nurse",
				"Home Fitness Trainer",
				"Home Hairdresser"
			};

			// French translations aligned by index with additionalCategories
			String[] additionalCategoriesFr = new String[] {
				"Plombier",
				"Électricien",
				"Menuisier",
				"Forgeron",
				"Technicien CVC",
				"Technicien gaz",
				"Réparation de chauffe-eau",
				"Réparation de portes et fenêtres",
				"Ouvrier du bâtiment",
				"Poseur de carrelage",
				"Poseur de plaques de plâtre / Décorateur",
				"Peintre",
				"Isolation thermique et étanchéité",
				"Pose de carreaux et céramique",
				"Démolition et rénovation",
				"Nettoyage de maison",
				"Nettoyage d’appartement après travaux",
				"Nettoyage de tapis et moquettes",
				"Nettoyage de réservoir d’eau",
				"Nettoyage de jardin",
				"Désinfection et lutte antiparasitaire",
				"Mécanicien auto",
				"Électricien auto",
				"Vidange d’huile",
				"Lavage auto à domicile",
				"Réparation de pneus",
				"Contrôle technique mobile",
				"Transport de marchandises et de meubles",
				"Camion avec chauffeur",
				"Monte-meubles",
				"Livraison locale",
				"Maintenance informatique",
				"Installation de réseau Wi-Fi",
				"Réparation de téléphone",
				"Installation de caméras de sécurité",
				"Développement de sites web",
				"Maintenance d’imprimantes",
				"Jardinier",
				"Tonte de pelouse",
				"Paysagiste",
				"Installation de système d’irrigation",
				"Élagage d’arbres",
				"Tailleur",
				"Photographe",
				"Décorateur d’intérieur",
				"Fabricant de meubles sur mesure",
				"Forgeron d’art",
				"Graveur",
				"Écrivain public",
				"Impression et photocopie",
				"Traduction",
				"Extraction de documents",
				"Traiteur",
				"Pâtissier traditionnel",
				"Location de chaises et tables",
				"DJ et musicien",
				"Nounou",
				"Infirmier(ère) à domicile",
				"Coach sportif à domicile",
				"Coiffeur à domicile"
			};

			// Arabic translations aligned by index with additionalCategories
			String[] additionalCategoriesAr = new String[] {
				"سباك",
				"كهربائي",
				"نجار",
				"حداد",
				"فني تكييف وتدفئة وتهوية",
				"فني غاز",
				"إصلاح سخان المياه",
				"إصلاح الأبواب والنوافذ",
				"عامل بناء",
				"مركب بلاط",
				"مركب جبس بورد / ديكور",
				"دهان",
				"عزل حراري ومائي",
				"تركيب البلاط والسيراميك",
				"هدم وتجديد",
				"تنظيف المنازل",
				"تنظيف الشقق بعد البناء",
				"تنظيف السجاد والموكيت",
				"تنظيف خزان المياه",
				"تنظيف الحدائق",
				"تعقيم ومكافحة الحشرات",
				"ميكانيكي سيارات",
				"كهربائي سيارات",
				"تغيير الزيت",
				"غسيل سيارات متنقل",
				"إصلاح الإطارات",
				"فحص تقني متنقل",
				"نقل البضائع والأثاث",
				"شاحنة مع سائق",
				"رافعة أثاث",
				"توصيل محلي",
				"صيانة الحاسوب",
				"تركيب شبكة واي فاي",
				"إصلاح الهواتف",
				"تركيب كاميرات المراقبة",
				"تطوير مواقع الويب",
				"صيانة الطابعات",
				"بستاني",
				"جز العشب",
				"مصمم حدائق",
				"تركيب نظام الري",
				"تقليم الأشجار",
				"خياط",
				"مصور",
				"مصمم داخلي",
				"صانع أثاث حسب الطلب",
				"حداد فني",
				"حفار/منقش",
				"كاتب عمومي",
				"طباعة وتصوير",
				"ترجمة",
				"استخراج الوثائق",
				"متعهد حفلات",
				"حلويات تقليدية",
				"تأجير الكراسي والطاولات",
				"دي جي وموسيقي",
				"مربية أطفال",
				"ممرض/ممرضة منزلي",
				"مدرب لياقة بدنية منزلي",
				"حلاق/حلاقة منزلي"
			};

			for (int i = 0; i < additionalCategories.length; i++) {
				String name = additionalCategories[i];
				String nameFr = additionalCategoriesFr[i];
				String nameAr = additionalCategoriesAr[i];
				if (!categoryRepository.existsByName(name)) {
					Category category = Category.builder()
						.name(name)
						.nameFr(nameFr)
						.nameAr(nameAr)
						.description(name)
						.descriptionFr(nameFr)
						.descriptionAr(nameAr)
						.build();
					categoryRepository.save(category);
				}
			}

			System.out.println("--> Seeded categories: " + categoryRepository.findAll().size());
		};
	}

//	@Bean
//	CommandLineRunner seedWorkers(WorkerRepository workerRepository, ServiceCategoryRepository serviceCategoryRepository) {
//		return args -> {
//			if (!workerRepository.existsById("worker1")) {
//				Worker w1 = new Worker();
//				w1.setUsername("worker1");
//				w1.setEmail("worker1@example.com");
//				w1.setFirstName("Ali");
//				w1.setLastName("Brahimi");
//				w1.setEnabled(1);
//				w1.setCreationDate(new Date());
//				w1.setPhoneNumber1("0550-000-001");
//				w1.setSpecialization("Plumber");
//				w1.setRating(4.5);
//				w1.setAvailable(true);
//				workerRepository.save(w1);
//			}
//
//			if (!workerRepository.existsById("worker2")) {
//				Worker w2 = new Worker();
//				w2.setUsername("worker2");
//				w2.setEmail("worker2@example.com");
//				w2.setFirstName("Nada");
//				w2.setLastName("Kaci");
//				w2.setEnabled(1);
//				w2.setCreationDate(new Date());
//				w2.setPhoneNumber1("0550-000-002");
//				w2.setSpecialization("Electrician");
//				w2.setRating(4.2);
//				w2.setAvailable(true);
//				workerRepository.save(w2);
//			}
//
//			if (!workerRepository.existsById("worker3")) {
//				Worker w3 = new Worker();
//				w3.setUsername("worker3");
//				w3.setEmail("worker3@example.com");
//				w3.setFirstName("Sami");
//				w3.setLastName("Meziani");
//				w3.setEnabled(1);
//				w3.setCreationDate(new Date());
//				w3.setPhoneNumber1("0550-000-003");
//				w3.setSpecialization("Carpenter");
//				w3.setRating(4.8);
//				w3.setAvailable(true);
//				workerRepository.save(w3);
//			}
//			if (!workerRepository.existsById("worker4")) {
//				Worker w4 = new Worker();
//				w4.setUsername("worker4");
//				w4.setEmail("worker4@example.com");
//				w4.setFirstName("Sami");
//				w4.setLastName("Meziani");
//				w4.setEnabled(1);
//				w4.setCreationDate(new Date());
//				w4.setPhoneNumber1("0550-000-003");
//				w4.setSpecialization("Carpenter");
//				w4.setRating(4.8);
//				w4.setAvailable(true);
//				w4.setCategory(serviceCategoryRepository.findById(Long.valueOf(1)).get());
//				w4.setLatitude(36.6699702);
//				w4.setLongitude(3.0570648);
//
//				workerRepository.save(w4);
//			}
//			if (!workerRepository.existsById("worker5")) {
//				Worker w5 = new Worker();
//				w5.setUsername("worker5");
//				w5.setEmail("worker5@example.com");
//				w5.setFirstName("Sami");
//				w5.setLastName("Meziani");
//				w5.setEnabled(1);
//				w5.setCreationDate(new Date());
//				w5.setPhoneNumber1("0550-000-003");
//				w5.setSpecialization("Carpenter");
//				w5.setRating(4.8);
//				w5.setAvailable(true);
//				w5.setCategory(serviceCategoryRepository.findById(Long.valueOf(1)).get());
//				w5.setLatitude(35.831852			);
//				w5.setLongitude(5.232194);
//
//				workerRepository.save(w5);
//			}
//
//			if (!workerRepository.existsById("worker6")) {
//				Worker worker6 = new Worker();
//				worker6.setUsername("worker6");
//				worker6.setPassword("worker6");
//				worker6.setEmail("worker5@example.com");
//				worker6.setFirstName("Sami");
//				worker6.setLastName("Meziani");
//				worker6.setEnabled(1);
//				worker6.setCreationDate(new Date());
//				worker6.setPhoneNumber1("0550-000-003");
//				worker6.setSpecialization("Carpenter");
//				worker6.setRating(4.8);
//				worker6.setAvailable(true);
//				worker6.setCategory(serviceCategoryRepository.findById(Long.valueOf(1)).get());
//				worker6.setLatitude(35.831852			);
//				worker6.setLongitude(5.232194);
//
//				workerRepository.save(worker6);
//			}
//			System.out.println("--> " + workerRepository.findAll().size() + " workers");
//		};
//	}

//	@Bean
//	CommandLineRunner seedClients(UserRepository userRepository) {
//		return args -> {
//			if (!userRepository.existsById("client1")) {
//				Client c1 = Client.builder().build();
//				c1.setUsername("client1");
//				c1.setEmail("client1@example.com");
//				c1.setFirstName("Amine");
//				c1.setLastName("Belkacem");
//				c1.setEnabled(1);
//				c1.setCreationDate(new Date());
//				c1.setPhoneNumber1("0661-000-001");
//				userRepository.save(c1);
//			}
//
//			if (!userRepository.existsById("client2")) {
//				Client c2 = Client.builder().build();
//				c2.setUsername("client2");
//				c2.setEmail("client2@example.com");
//				c2.setFirstName("Sara");
//				c2.setLastName("Haddad");
//				c2.setEnabled(1);
//				c2.setCreationDate(new Date());
//				c2.setPhoneNumber1("0661-000-002");
//				userRepository.save(c2);
//			}
//
//			if (!userRepository.existsById("client3")) {
//				Client c3 = Client.builder().build();
//				c3.setUsername("client3");
//				c3.setEmail("client3@example.com");
//				c3.setFirstName("Yacine");
//				c3.setLastName("Zerrouki");
//				c3.setEnabled(1);
//				c3.setCreationDate(new Date());
//				c3.setPhoneNumber1("0661-000-003");
//				userRepository.save(c3);
//			}
//
//			if (!userRepository.existsById("client4")) {
//				Client c4 = Client.builder().build();
//				c4.setUsername("client4");
//				c4.setEmail("client4@example.com");
//				c4.setFirstName("Yacine");
//				c4.setLastName("Zerrouki");
//				c4.setEnabled(1);
//				c4.setCreationDate(new Date());
//				c4.setPhoneNumber1("0661-000-003");
//				c4.setLatitude(36.652520);
//				c4.setLongitude(3.091661);
//				userRepository.save(c4);
//			}
//
//			if (!userRepository.existsById("dekarb2")) {
//				Client c5 = Client.builder().build();
//				c5.setUsername("dekarb2");
//				c5.setPassword("dekarb2");
//				c5.setEmail("dekarb2@example.com");
//				c5.setFirstName("Yacine");
//				c5.setLastName("Zerrouki");
//				c5.setEnabled(1);
//				c5.setCreationDate(new Date());
//				c5.setPhoneNumber1("0661-000-003");
//				c5.setLatitude(36.652520);
//				c5.setLongitude(3.091661);
//				userRepository.save(c5);
//			}
//			System.out.println("--> Seeded clients. Total users: " + userRepository.findAll().size());
//		};
//	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}

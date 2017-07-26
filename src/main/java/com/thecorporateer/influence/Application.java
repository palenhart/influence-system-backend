package com.thecorporateer.influence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Department;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Rank;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;
import com.thecorporateer.influence.repositories.RankRepository;

@SpringBootApplication(scanBasePackages = { "com.thecorporateer.influences" })
public class Application {

	@Autowired
	private RankRepository rankRepository;
	@Autowired
	private InfluenceTypeRepository influenceTypeRepository;
	@Autowired
	private CorporateerRepository corporateerRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private DivisionRepository divisionRepository;
	@Autowired
	private InfluenceRepository influenceRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener(ContextRefreshedEvent.class)
	public void initialize() {
		List<Rank> l = new ArrayList<>();

		l.add(new Rank("Junior Associate", 50));
		l.add(new Rank("Associate", 100));
		l.add(new Rank("Senior Associate", 200));
		l.add(new Rank("Manager", 400));
		l.add(new Rank("Director", 800));
		l.add(new Rank("Board Member", 1600));
		l.add(new Rank("CEO", 1600));

		l = rankRepository.save(l);

		List<InfluenceType> types = new ArrayList<>();

		types.add(new InfluenceType("GENERAL", false));
		types.add(new InfluenceType("GENERAL_DEMERIT", false));
		types.add(new InfluenceType("DIVISION", true));
		types.add(new InfluenceType("DIVISION_DEMERIT", true));
		types.add(new InfluenceType("DEPARTMENT", true));
		types.add(new InfluenceType("DEPARTMENT_DEMERIT", true));

		types = influenceTypeRepository.save(types);

		List<Department> d = new ArrayList<>();

		d.add(new Department("none"));
		d.add(new Department("Exploration"));
		d.add(new Department("Business"));
		d.add(new Department("Security"));
		d.add(new Department("Resources"));
		d.add(new Department("Social"));
		d.add(new Department("Support"));
		d.add(new Department("Public Relations"));

		d = departmentRepository.save(d);

		List<Division> div = new ArrayList<>();

		div.add(new Division("Board", departmentRepository.findByName("none")));
		div.add(new Division("Cartograhy", departmentRepository.findByName("Exploration")));
		div.add(new Division("Prospecting", departmentRepository.findByName("Exploration")));
		div.add(new Division("Research", departmentRepository.findByName("Exploration")));

		div.add(new Division("Contracts", departmentRepository.findByName("Business")));
		div.add(new Division("Finance", departmentRepository.findByName("Business")));
		div.add(new Division("Trade", departmentRepository.findByName("Business")));

		div.add(new Division("CSOC", departmentRepository.findByName("Security")));
		div.add(new Division("Ground", departmentRepository.findByName("Security")));
		div.add(new Division("Repossesion", departmentRepository.findByName("Security")));
		div.add(new Division("Space", departmentRepository.findByName("Security")));

		div.add(new Division("Development", departmentRepository.findByName("Resources")));
		div.add(new Division("Extraction", departmentRepository.findByName("Resources")));
		div.add(new Division("Transport", departmentRepository.findByName("Resources")));

		div.add(new Division("Diplomacy", departmentRepository.findByName("Social")));
		div.add(new Division("HR", departmentRepository.findByName("Social")));
		div.add(new Division("Training", departmentRepository.findByName("Social")));

		div.add(new Division("CSAR", departmentRepository.findByName("Support")));
		div.add(new Division("Engineering", departmentRepository.findByName("Support")));
		div.add(new Division("IT", departmentRepository.findByName("Support")));

		div.add(new Division("e-Sports", departmentRepository.findByName("Public Relations")));
		div.add(new Division("Media", departmentRepository.findByName("Public Relations")));

		div = divisionRepository.save(div);

		Corporateer c = new Corporateer();
		c.setName("Peter");
		c.setTributes(5);
		c.setRank(l.get(0));
		c.setMainDivision(div.get(0));

		c = corporateerRepository.save(c);

		List<Influence> inf = new ArrayList<>();
		for (int j = 0; j < types.size(); j++) {
			if (types.get(j).getName().contains("DIVISION")) {
				for (int i = 1; i < div.size(); i++) {
					inf.add(new Influence(c, div.get(i).getDepartment(), div.get(i), types.get(j), 0));
				}
			} else if (types.get(j).getName().contains("DEPARTMENT")) {
				for (int i = 1; i < d.size(); i++) {
					inf.add(new Influence(c, d.get(i), div.get(0), types.get(j), 0));
				}
			} else if (types.get(j).getName().contains("GENERAL")) {
				inf.add(new Influence(c, d.get(0), div.get(0), types.get(j), 0));
			}
		}
		inf = influenceRepository.save(inf);

	}
}

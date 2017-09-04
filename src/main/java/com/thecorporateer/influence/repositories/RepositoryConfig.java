package com.thecorporateer.influence.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.thecorporateer.influence.objects.JpaEntity;

/**
 * @author Zollak
 *
 *         Configuration for REST repositories
 *
 */
@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
		provider.addIncludeFilter(new AssignableTypeFilter(JpaEntity.class));
		Set<BeanDefinition> components = provider.findCandidateComponents("com.thecorporateer.influence");
		List<Class<?>> classes = new ArrayList<>();

		components.forEach(component -> {
			try {
				classes.add(Class.forName(component.getBeanClassName()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		config.exposeIdsFor(classes.toArray(new Class[classes.size()]));
	}
}
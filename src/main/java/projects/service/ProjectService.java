package projects.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

public class ProjectService {
	private ProjectDao projectDao = new ProjectDao();

	public Project addProject(Project project) {// method to call DAO class to insert row
		return projectDao.insertProject(project);
	}

	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}

	// method that calls the dao to retrieve a singel project
	// with all details will throw excetpion if project w id
	// does not exist
	public Project fetchProjectById(Integer projectId) {
		return projectDao.fetchProjectById(projectId).orElseThrow(() -> new NoSuchElementException("Project with project ID=" + projectId + " does not exist."));
				

	}

	//method that calls dao 
	public void modifyProjectDetails(Project project) { 
		if (!projectDao.modifyProjectDetails(project)) {//calls dao method to return boolean to indicate whether udpate
			throw new DbException("Project with ID=" + project.getProjectId() + " does not exist.");//operation was successfull.
		}//checks return value  and if false throws DbException saying project doesnt exist
	}

	public void deleteProject(Integer projectId) {
		if(!projectDao.deleteProject(projectId)) {//calls deleteProject from projectDao,
			//checks the boolean value, if false, am exception is thrown stating message below
			throw new DbException("Project with ID= " + projectId + " does not exist." );
		}
	}
}

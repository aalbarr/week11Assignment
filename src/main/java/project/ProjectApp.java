package project;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;

	//@formatter:off
	private List<String> operations = List.of(   //creates a list that serves as menu options
			"1) Add a project",
			"2) List projects",
			"3) Select a project",
			"4) Update project details"
			);
	//@formatter:on

	public static void main(String[] args) {
		new ProjectApp().processUserSelections();

	}

	private void processUserSelections() {// displays menu selections, gets the selection and then processes said
											// selection
		boolean done = false;

		while (!done) {// try catch to catch exception
			try {
				int selection = getUserSelection();

				switch (selection) {
				case -1:
					done = exitMenu();
					break;

				case 1:
					createProject();
					break;

				case 2:
					listProjects();
					break;

				case 3:
					selectProject();
					break;
					
				case 4:
					updateProjectDetails();
					break;

				default:
					System.out.println("\nError " + selection + " Try again.");
					break;
				}

			} catch (Exception e) {
				System.out.println("\nError: " + e + " Try again.");
				e.printStackTrace();
			}
		}
	}

private void updateProjectDetails() {//allows user to update project details
	if(Objects.isNull(curProject)) {//checks to see if curProject is null
		System.out.println("\nPlease select a project");
		return;
	}
	//prints a message along w current setting in curProject
	String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
	BigDecimal estimatedHours = getDecimalInput("Enter estimated hours [" + curProject.getEstimatedHours() + "]");
	BigDecimal actualHours = getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
	Integer difficulty = getIntInput("Enter the difficulty [" + curProject.getDifficulty() + "]");
	String notes = getStringInput("Enter any notes [" + curProject.getNotes() + "]");
	
	Project project = new Project();//creates new Project object
	
	project.setProjectId(curProject.getProjectId());//sets project ID field  in project object to value in curProject object
	
	//checks user input if not null adds value to Project object if null adds value from curProject
	project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
	project.setEstimatedHours(Objects.isNull(estimatedHours)
			? curProject.getEstimatedHours() : estimatedHours);
	project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
	project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
	project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
	
	projectService.modifyProjectDetails(project);
	
	curProject = projectService.fetchProjectById(curProject.getProjectId());
	
	}

//method will list project IDs and names so user can select an ID
	private void selectProject() {
		listProjects();// calls listProjects to method
		// this collects ID from user and assigns it to an Integer variable
		Integer projectId = getIntInput("Enter a project ID to select a project");

		curProject = null;// sets curProject to null to unselect any selected project

		// calls method, should return a project object and assign the reutnred object
		// to curproject
		// will throw NoSuchElementExcetpion if invalid project ID is entered
		curProject = projectService.fetchProjectById(projectId);

	}

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		System.out.println("\nProjects:");

		projects.forEach(
				project -> System.out.println("   " + project.getProjectId() + ": " + project.getProjectName()));
	}

	private void createProject() {
		String projectName = getStringInput("Enter the project ");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty(1 - 5)"); // there is no code to verify if valid
		String notes = getStringInput("Enter the project notes");

		Project project = new Project();

		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);

		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);

	}

	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}

	}

	private boolean exitMenu() {
		System.out.println("Exiting the menu");
		return true;
	}

	private int getUserSelection() {// this method prints operation & accepts user input as an Integer
		printOperations(); // method call to said method
		Integer input = getIntInput("Enter a menu selection"); // calls to methodretuns user's menu selection
		return Objects.isNull(input) ? -1 : input;
	}

	private Integer getIntInput(String prompt) {// takes user input and returns it as an integer value
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}

	}

	private String getStringInput(String prompt) {
		System.out.print(prompt + ":");
		String input = scanner.nextLine();

		return input.isBlank() ? null : input.trim();
	}

	private void printOperations() {
		System.out.println("\nThese are the available selctions. Press the Enter key to quit: ");

		operations.forEach(line -> System.out.println("  " + line));

		if (Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		} else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}
}
package no.perhenrik.replayplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ReplayRepository {
	
	public interface StepCommand {
		public ReturnValue execute(List<Step> steps, int index, Location location);
	}
	
	private HashMap<String, List<Step>> repo = null;
	private String selectedReplay = null;
	private int selectedStep = -1;
	
	public ReplayRepository() {
		setRepo(new HashMap<String, List<Step>>());
	}
	
	public ReplayRepository(HashMap<String, List<Step>> repo) {
		setRepo(repo);
	}
	
	@SuppressWarnings("unchecked")
	public ReplayRepository(Map<String, Object> map) {
		HashMap<String, List<Step>> repo = new HashMap<String, List<Step>>();
		for (String key : map.keySet()) {
			repo.put(key, (List<Step>) map.get(key));
		}
		setRepo(repo);
	}
	
	public Set<String> list() {
		return getRepo().keySet();		
	}

	public void create(String id) {
		getRepo().put(id, new ArrayList<Step>());
	}

	public ReturnValue select(String id) {
		ReturnValue ret = new ReturnValue();
		
		if(getRepo().containsKey(id)) {
			setSelectedReplay(id);
		} else {
			ret.setOk(false).setMessage(new StringBuilder().append("The Replay with id '").append(id).append("' does not exist").toString());
		}
		return ret;
	}

	public void delete(String id) {
		getRepo().remove(id);
		setSelectedReplay(null);
	}

	public List<Step> steps() {
		List<Step> ret = null;
		if(getSelectedReplay() != null) {
			ret = getRepo().get(selectedReplay);
		}
		if(ret == null) {
			ret = new ArrayList<Step>();
		}
		return ret;
	}

	public ReturnValue addStep(Location location) {
		return insertStep("9999", location); // insert at a high index so it will go last
	}

	private ReturnValue callStepCommand(StepCommand command, String index, Location location) {
		List<Step> steps = null;
		ReturnValue ret = new ReturnValue();
		Integer idx = getIntegerValue(index, ret);
		if(ret.isOk()) {
			steps = getSteps(ret);
			if(ret.isOk()) {
				idx = ( idx < 0 ? 0 : idx);
				idx = ( idx > steps.size() ? steps.size()  : idx);
				return command.execute(steps, idx, location);
			}
		}
		return ret;
	}
	
	private class InsertStepCommand implements StepCommand {
		@Override
		public ReturnValue execute(List<Step> steps, int index, Location location) {
			ReturnValue ret = new ReturnValue();
			steps.add(index, new Step(location));
			setSelectedStep(index);
			ret.setMessage(new StringBuilder().append("New step added with index ").append(index).toString());
			return ret;
		}	
	}
	
	public ReturnValue insertStep(String index, Location location) {
		return callStepCommand(new InsertStepCommand(), index, location);
	}

	private class DeleteStepCommand implements StepCommand {
		@Override
		public ReturnValue execute(List<Step> steps, int index, Location location) {
			ReturnValue ret = new ReturnValue();
			steps.remove(index);
			setSelectedStep(index);
			ret.setOk(true).setMessage(new StringBuilder().append("Removed step with index ").append(index).toString());
			return ret;
		}	
	}
	
	public ReturnValue deleteStep(String index) {
		return callStepCommand(new DeleteStepCommand(), index, null);
	}

	private class NextStepCommand implements StepCommand {
		@Override
		public ReturnValue execute(List<Step> steps, int index, Location location) {
			ReturnValue ret = new ReturnValue();
			setSelectedStep(getSelectedStep() + 1);
			ret.setOk(true).setMessage(new StringBuilder().append("Moved to step with index ").append(getSelectedStep()).toString());
			return ret;
		}	
	}
	
	public ReturnValue next() {
		return callStepCommand(new NextStepCommand(), "-1", null);
	}

	private class PreviousStepCommand implements StepCommand {
		@Override
		public ReturnValue execute(List<Step> steps, int index, Location location) {
			ReturnValue ret = new ReturnValue();
			setSelectedStep(getSelectedStep() - 1);
			ret.setOk(true).setMessage(new StringBuilder().append("Moved to step with index ").append(getSelectedStep()).toString());
			return ret;
		}	
	}
	
	public ReturnValue previous() {
		return callStepCommand(new PreviousStepCommand(), "-1", null);
	}

	private class FirstStepCommand implements StepCommand {
		@Override
		public ReturnValue execute(List<Step> steps, int index, Location location) {
			ReturnValue ret = new ReturnValue();
			setSelectedStep(0);
			ret.setOk(true).setMessage(new StringBuilder().append("Moved to step with index ").append(getSelectedStep()).toString());
			return ret;
		}	
	}
	
	public ReturnValue first() {
		return callStepCommand(new FirstStepCommand(), "-1", null);
	}

	public Step back() {
		return this.getCurrentStep();
	}

	public Step getStepWithIndex(int index) {
		List<Step> steps = this.getSteps();
		try {
			return steps.get(index);
		} catch(IndexOutOfBoundsException ex) {
			return null;
		}
	}
	
	public int getSelectedStep() {
		List<Step> steps = repo.get(getSelectedReplay());
		if(steps == null) {
			this.selectedStep = -1;
		} else {
			this.selectedStep = ( this.selectedStep < 0 ? 0 : this.selectedStep);
			this.selectedStep = ( this.selectedStep > steps.size() ? steps.size() : this.selectedStep);
		}
		return this.selectedStep;
	}

	public void setSelectedStep(int selectedStep) {
		List<Step> steps = this.getSteps();
		if(steps == null) {
			this.selectedStep = -1;
		} else {
			this.selectedStep = ( selectedStep < 0 ? 0 : selectedStep);
			this.selectedStep = ( selectedStep > steps.size() - 1 ? steps.size() - 1 : selectedStep);
		}
	}

	public String getSelectedReplay() {
		return selectedReplay;
	}

	public void setSelectedReplay(String selectedReplays) {
		this.selectedReplay = selectedReplays;
	}

	public HashMap<String, List<Step>> getRepo() {
		return repo;
	}

	public void setRepo(HashMap<String, List<Step>> repo) {
		this.repo = repo;
	}

	private List<Step> getSteps() {
		return this.getSteps(new ReturnValue());
	}
	
	private List<Step> getSteps(ReturnValue ret) {
		List<Step> steps = new ArrayList<Step>();
		
		if(getSelectedReplay() != null) {
			steps = getRepo().get(getSelectedReplay());
			if(steps == null) {
				steps = new ArrayList<Step>();
				getRepo().put(getSelectedReplay(), steps);
			}
		} else {
			ret.setOk(false).setMessage(new StringBuilder().append(ret.getMessage()).append("You must select a Replay to add a step.\n").toString());
		}
		return steps;
	}

	private Integer getIntegerValue(String index, ReturnValue ret) {
		Integer value = -1;
		try {
			value = Integer.parseInt(index);
		} catch(NumberFormatException ex) {
			ret.setOk(false).setMessage(new StringBuffer().append(ret.getMessage()).append("'").append(index).append("' is not an integer.\n").toString());
		}
		return value;
	}

	public Step getCurrentStep() {
		return this.getStepWithIndex(this.getSelectedStep());
	}
}

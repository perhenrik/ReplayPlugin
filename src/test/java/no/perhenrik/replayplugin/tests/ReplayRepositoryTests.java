package no.perhenrik.replayplugin.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.junit.Before;
import org.junit.Test;

import no.perhenrik.replayplugin.ReplayRepository;
import no.perhenrik.replayplugin.ReturnValue;
import no.perhenrik.replayplugin.Step;

public class ReplayRepositoryTests {

	private ReplayRepository repo;
	
	@Before
	public void setUp() throws Exception {
		repo = new ReplayRepository();
		HashMap<String, List<Step>> map = new HashMap<String, List<Step>>();
		List<Step> list = new ArrayList<Step>();
		list.add(new Step(null, 0, 0, 0, 0, 0));
		list.add(new Step(null, 1, 1, 1, 1, 1));
		list.add(new Step(null, 2, 2, 2, 2, 2));
		map.put("test1", list);
		map.put("test2", list);
		repo.setRepo(map);
	}

	@Test
	public void testReplayRepository() {
		ReplayRepository repo = new ReplayRepository();
		assertNotEquals(repo, null);
	}

	@Test
	public void testList() {
		Set<String> list = repo.list();
		assertEquals(2, list.size());
	}

	@Test
	public void testCreate() {
		repo.create("foobar");
		Set<String> list = repo.list();
		boolean containsFoobar = list.contains("foobar");
		assertEquals(true, containsFoobar);
	}

	@Test
	public void testSelect() {
		ReturnValue ret = repo.select("test2");
		assertEquals(true, ret.isOk());
		assertEquals("test2", repo.getSelectedReplay());
	}

	@Test
	public void testDelete() {
		repo.delete("test2");
		Set<String> list = repo.list();
		boolean containsTest2 = list.contains("test2");
		assertEquals(false, containsTest2);
	}

	@Test
	public void testSteps() {
		repo.select("test1");
		List<Step> list = repo.steps();
		assertEquals(3, list.size());
	}

	@Test
	public void testAddStep() {
		repo.select("test1");
		repo.addStep(new Location(null, 3, 3, 3));
		List<Step> list = repo.steps();
		assertEquals(4, list.size());
	}

	@Test
	public void testInsertStep() {
		repo.select("test1");
		repo.insertStep("0", new Location(null, 3, 3, 3));
		List<Step> list = repo.steps();
		Location first = list.get(0);
		assertEquals(3, first.getX(), 0.01);
	}

	@Test
	public void testDeleteStep() {
		repo.select("test1");
		repo.deleteStep("0");
		List<Step> list = repo.steps();
		Location first = list.get(0);
		assertEquals(1, first.getX(), 0.01);
	}

	@Test
	public void testNext() {
		repo.select("test1");
		repo.setSelectedStep(1);
		ReturnValue ret = repo.next();
		assertEquals(true, ret.isOk());
		assertEquals(2, repo.getSelectedStep());
	}

	@Test
	public void testPrevious() {
		repo.select("test1");
		repo.setSelectedStep(1);
		ReturnValue ret = repo.previous();
		assertEquals(true, ret.isOk());
		assertEquals(0, repo.getSelectedStep());
	}

	@Test
	public void testFirst() {
		repo.select("test1");
		repo.setSelectedStep(1);
		ReturnValue ret = repo.first();
		assertEquals(true, ret.isOk());
		assertEquals(0, repo.getSelectedStep());
	}

	@Test
	public void testBack() {
		repo.select("test1");
		repo.setSelectedStep(1);
		Step step = repo.back();
		assertEquals(1, step.getX(), 0.01);
		assertEquals(1, repo.getSelectedStep());
	}

}

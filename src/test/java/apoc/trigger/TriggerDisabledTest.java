package apoc.trigger;

import apoc.util.TestUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.test.rule.DbmsRule;
import org.neo4j.test.rule.ImpermanentDbmsRule;

import static apoc.ApocConfig.APOC_TRIGGER_ENABLED;
import static apoc.ApocConfig.apocConfig;

/**
 * @author alexiudice
 * @since 14.07.18
 * <p>
 * Tests for fix of #845.
 * <p>
 * Testing disabled triggers needs to be a different test file from 'TriggerTest.java' since
 * Trigger classes and methods are static and 'TriggerTest.java' instantiates a class that enables triggers.
 *
 * NOTE: this test class expects every method to fail with a RuntimeException
 */
public class TriggerDisabledTest {

    @Rule
    public DbmsRule db = new ImpermanentDbmsRule();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        apocConfig().setProperty(APOC_TRIGGER_ENABLED, false);
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage(TriggerHandler.NOT_ENABLED_ERROR);
        TestUtil.registerProcedure(db, Trigger.class);
    }

    @Test
    public void testTriggerDisabledList() {
        db.execute("CALL apoc.trigger.list() YIELD name RETURN name").close();
    }

    @Test
    public void testTriggerDisabledAdd() {
        db.execute("CALL apoc.trigger.add('test-trigger', 'RETURN 1', {phase: 'before'}) YIELD name RETURN name");
    }

    @Test
    public void testTriggerDisabledRemove() {
        db.execute("CALL apoc.trigger.remove('test-trigger')").close();
    }

    @Test
    public void testTriggerDisabledResume() {
        db.execute("CALL apoc.trigger.resume('test-trigger')").close();
    }

    @Test
    public void testTriggerDisabledPause() {
        db.execute("CALL apoc.trigger.pause('test-trigger')").close();
    }
}

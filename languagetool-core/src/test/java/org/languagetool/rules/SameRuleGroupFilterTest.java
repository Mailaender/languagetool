/* LanguageTool, a natural language style checker
 * Copyright (C) 2012 Daniel Naber (http://www.danielnaber.de)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package org.languagetool.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.languagetool.Language;
import org.languagetool.TestTools;
import org.languagetool.rules.patterns.Element;
import org.languagetool.rules.patterns.PatternRule;

public class SameRuleGroupFilterTest extends TestCase {

  private static final Language language = TestTools.getDemoLanguage();

  public void testFilter() {
    final List<Element> fakeElements = new ArrayList<>();
    final PatternRule rule1 = new PatternRule("id1", language, fakeElements, "desc1", "msg1", "shortMsg1");
    final PatternRule rule2 = new PatternRule("id1", language, fakeElements, "desc2", "msg2", "shortMsg2");
    final RuleMatch match1 = new RuleMatch(rule1, 10, 20, "Match1");
    final RuleMatch match2 = new RuleMatch(rule2, 15, 25, "Match2");
    final SameRuleGroupFilter filter = new SameRuleGroupFilter();
    final List<RuleMatch> filteredMatches = filter.filter(Arrays.asList(match1, match2));
    assertEquals(1, filteredMatches.size());
    assertEquals("Match1", filteredMatches.get(0).getMessage());
  }

  public void testNoFilteringIfNotOverlapping() {
    final List<Element> fakeElements = new ArrayList<>();
    final PatternRule rule1 = new PatternRule("id1", language, fakeElements, "desc1", "msg1", "shortMsg1");
    final PatternRule rule2 = new PatternRule("id1", language, fakeElements, "desc2", "msg2", "shortMsg2");
    final RuleMatch match1 = new RuleMatch(rule1, 10, 20, "Match1");
    final RuleMatch match2 = new RuleMatch(rule2, 21, 25, "Match2");
    final SameRuleGroupFilter filter = new SameRuleGroupFilter();
    final List<RuleMatch> filteredMatches = filter.filter(Arrays.asList(match1, match2));
    assertEquals(2, filteredMatches.size());
  }

  public void testNoFilteringIfDifferentRulegroups() {
    final List<Element> fakeElements = new ArrayList<>();
    final Rule rule1 = new PatternRule("id1", language, fakeElements, "desc1", "msg1", "shortMsg1");
    final Rule rule2 = new PatternRule("id2", language, fakeElements, "desc2", "msg2", "shortMsg2");
    final RuleMatch match1 = new RuleMatch(rule1, 10, 20, "Match1");
    final RuleMatch match2 = new RuleMatch(rule2, 15, 25, "Match2");
    final SameRuleGroupFilter filter = new SameRuleGroupFilter();
    final List<RuleMatch> filteredMatches = filter.filter(Arrays.asList(match1, match2));
    assertEquals(2, filteredMatches.size());
  }

  public void testOverlaps() {
    final SameRuleGroupFilter filter = new SameRuleGroupFilter();

    assertTrue(filter.overlaps(makeRuleMatch(10, 20), makeRuleMatch(10, 20)));
    assertTrue(filter.overlaps(makeRuleMatch(10, 20), makeRuleMatch(5, 11)));
    assertTrue(filter.overlaps(makeRuleMatch(10, 20), makeRuleMatch(19, 21)));
    assertTrue(filter.overlaps(makeRuleMatch(10, 20), makeRuleMatch(11, 19)));
    assertTrue(filter.overlaps(makeRuleMatch(10, 20), makeRuleMatch(1, 10)));
    assertTrue(filter.overlaps(makeRuleMatch(10, 20), makeRuleMatch(20, 20)));

    assertFalse(filter.overlaps(makeRuleMatch(10, 20), makeRuleMatch(21, 30)));
    assertFalse(filter.overlaps(makeRuleMatch(10, 20), makeRuleMatch(1, 9)));
  }

  private RuleMatch makeRuleMatch(int fromPos, int toPos) {
    return new RuleMatch(null, fromPos, toPos, "FakeMatch1");
  }

}

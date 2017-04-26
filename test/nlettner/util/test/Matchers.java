package nlettner.util.test;

import javafx.util.Pair;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Matchers {
	protected static class HasItemsInOrder<T> extends TypeSafeMatcher<Iterable<T>> {

		protected T[] elements;
		protected ArrayList<Pair<Integer, T>> violations;

		public HasItemsInOrder(T ... elements) {
			this.elements = elements;
		}

		protected boolean compareElements(T a, T b) {
			return (a == null && b == null) || (a != null && a.equals(b));
		}

		@Override
		public void describeTo(Description description) {
			int infractions = 0;
			Iterator<Pair<Integer, T>> iter = violations.iterator();
			for (; iter.hasNext() && infractions < 3; infractions++) {
				Pair<Integer, T> violation = iter.next();
				int index = violation.getKey();
				description.appendText("\n\t")
						.appendText("(")
						.appendText(Integer.toString(index))
						.appendText(") ")
						.appendValue(violation.getValue())
						.appendText(" != ");
				if (index < elements.length) {
					description.appendValue(elements[index].toString());
				} else {
					description.appendValue(null);
				}
			}

			if (infractions < violations.size()) {
				description.appendText("\n\t")
						.appendText(" ... (and ")
						.appendText(Integer.toString(violations.size() - infractions))
						.appendText(" others)");
			}
		}

		@Override
		public boolean matchesSafely(Iterable<T> iterable) {
			int index = 0;
			violations = new ArrayList<Pair<Integer, T>>();

			for (Iterator<T> iter = iterable.iterator(); iter.hasNext(); index++) {
				T element = iter.next();
				if (index >= elements.length || !compareElements(elements[index], element)) {
					violations.add(new Pair<>(index, element));
				}
			}

			while (index < elements.length) {
				violations.add(new Pair<>(index, null));
				index++;
			}

			return violations.isEmpty();
		}
	}

	public static <T> HasItemsInOrder<T> hasItemsInOrder(T ... elements) {
		return new HasItemsInOrder<>(elements);
	}


	protected static class HasAnyOf<T> extends TypeSafeMatcher<Iterable<T>> {
		protected Set<T> elements;

		public HasAnyOf(T ... elements) {
			this.elements = new HashSet<>();

			for (T element : elements) {
				this.elements.add(element);
			}
		}

		@Override
		public void describeTo(Description description) {
			description.appendText(" has at least one of:\n\t")
					.appendText(elements.toString());
		}

		@Override
		public boolean matchesSafely(Iterable<T> iterable) {
			boolean foundAny = false;
			Iterator<T> items = iterable.iterator();

			while (items.hasNext() && !foundAny) {
				foundAny = foundAny || this.elements.contains(items.next());
			}

			return foundAny;
		}
	}

	public static <T> HasAnyOf<T> hasAnyOf(T ... elements) {
		return new HasAnyOf<>(elements);
	}
}

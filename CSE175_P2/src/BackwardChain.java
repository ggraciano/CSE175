//
// BackwardChain
//
// This class implements a backward chaining inference procedure. The
// implementation is very skeletal, and the resulting reasoning process is
// not particularly efficient. Knowledge is restricted to the form of
// definite clauses, grouped into a list of positive literals (facts) and
// a list of Horn clause implications (rules). The inference procedure
// maintains a list of goals. On each step, a proof is sought for the
// first goal in this list, starting by an attempt to unify the goal with
// any known fact in the knowledge base. If this fails, the rules are
// examined in the order in which they appear in the knowledge base, searching
// for a consequent that unifies with the goal. Upon successful unification,
// a proof is sought for the conjunction of the rule antecedents. If this
// fails, further rules are considered. Note that this is a strictly
// depth-first approach, so it is incomplete. Note, also, that there is
// no backtracking with regard to matches to facts -- the first match is
// always taken and other potential matches are ignored. This can make
// the algorithm incomplete in another way. In short, the order in which
// facts and rules appear in the knowledge base can have a large influence
// on the behavior of this inference procedure.
//
// In order to use this inference engine, the knowledge base must be
// initialized by a call to "initKB". Queries are then submitted using the
// "ask" method. The "ask" function returns a binding list which includes
// bindings for intermediate variables.
//
// David Noelle -- Tue Oct  9 18:48:57 PDT 2018
//


import java.util.*;


public class BackwardChain {

    public KnowledgeBase kb;

	// Default constructor ...
	public BackwardChain() {
		this.kb = new KnowledgeBase();
	}

	// initKB -- Initialize the knowledge base by interactively requesting
	// file names and reading those files. Return false on error.
	public boolean initKB() {
		return (kb.readKB());
	}

	// unify -- Return the most general unifier for the two provided literals,
	// or null if no unification is possible. The returned binding list
	// should be freshly allocated.
	public BindingList unify(Literal lit1, Literal lit2, BindingList bl) {

		// PROVIDE YOUR CODE HERE!
		
		// References: Artificial Intelligence: A Modern Approach
		// 		Page 328
		
		// function UNIFY(x, y, θ) returns a substitution to make x and y identical
		// 	inputs: x, a variable, constant, list, or compound expression
		// 			y, a variable, constant, list, or compound expression
		// 			θ, the substitution built up so far (optional, defaults to empty)
		// 	if θ = failure then return failure
		// 	else if x = y then return θ
		
		// If the binding list is empty, return null.
		if (bl == null) {
			return null;
		}
		
		// If the literals have the same predicate, they will bind. As such,
		// return their unification.
		if (lit1.pred.equals(lit2.pred)) {
			return unify(lit1.args, lit2.args, bl);
		}

		return (null);
	}

	// unify -- Return the most general unifier for the two provided terms,
	// or null if no unification is possible. The returned binding list
	// should be freshly allocated.
	public BindingList unify(Term t1, Term t2, BindingList bl) {

		// PROVIDE YOUR CODE HERE!
		
		// References: Artificial Intelligence: A Modern Approach
		// 		Page 328
		
		// function UNIFY(x, y, θ) returns a substitution to make x and y identical
		// 	inputs: x, a variable, constant, list, or compound expression
		// 			y, a variable, constant, list, or compound expression
		// 			θ, the substitution built up so far (optional, defaults to empty)
		// 	if VARIABLE?(x) then return UNIFY-VAR(x, y, θ)
		// 	else if VARIABLE?(y) then return UNIFY-VAR(y, x, θ)
		
		// function UNIFY-VAR(var, x, θ) returns a substitution
		// 	if {var/val} ∈ θ then return UNIFY(val, x, θ)
		// 	else if {x/val} ∈ θ then return UNIFY(var, val, θ)
		// 	else if OCCUR-CHECK?(var, x) then return failure
		// 	else return add {var/x} to θ
		
		// If the binding list is empty, return null.
		if (bl == null) {
			return null;
		}
		
		// If the terms are the same, then the binding list does not change.
		if (t1.equals(t2)) {
			return bl;
		}
		
		// Create a new instance of the binding list, bl.
		BindingList modified_bl = new BindingList(bl);
		
		// Check if t1 is a constant.
		if (t1.c != null) {
			// Check if t2 is a constant or variable.
			if (t2.c != null) {
				// If the terms are the same, then the binding list does not
				// change.
				if (t1.equals(t2)) {
					return bl;
				}
			} else if (t2.v != null) {
				// If t2 is a variable, then it can bind with t1, or the
				// constant.
				return unify(t2, t1, modified_bl);
			}
		}
		
		// Check if t1 is a variable.
		if (t1.v != null) {
			// Check if t1 is bound.
			Term temp1 = bl.boundValue(t1.v);
			
			// If t1 is bound, unify the t2 and temp1, or the bound term of t1.
			// If not, then check if t2 is a constant or variable.
			if (temp1 != null) {
				return unify(t2, temp1, modified_bl);
			} else if (t2.c != null) {
				// t1 can bind with t2, so add it to the modified binding list.
				modified_bl.addBinding(t1.v, t2);
				return modified_bl;
			} else if (t2.v != null) {
				// If the terms are the same, then the binding list does not
				// change.
				if (t1.equals(t2)) {
					return bl;
				}
				
				// Check if t2 is bound.
				Term temp2 = bl.boundValue(t2.v);
				
				// If t2 is bound, unify t1 and temp2, or the bound term of t2.
				// If not, then add it to the modified binding list.
				if (temp2 != null) {
					return unify(t1, temp2, modified_bl);
				} else {
					modified_bl.addBinding(t2.v, t1);
					return modified_bl;
				}
			}
		}
		
		// Check if t1 is a function.
		if (t1.f != null) {
			// Check if t2 is a constant, variable, or function.
			if (t2.c != null) {
				// A constant cannot bind with a function.
				return null;
			} else if (t2.v != null) {
				// Bind the variable with the function.
				return unify(t2, t1, modified_bl);
			} else if (t2.f != null) {
				// Bind the function terms.
				return unify(t1.f, t2.f, modified_bl);
			}
		}

		return (null);
	}

	// unify -- Return the most general unifier for the two provided functions,
	// or null if no unification is possible. The returned binding list
	// should be freshly allocated.
	public BindingList unify(Function f1, Function f2, BindingList bl) {

		// PROVIDE YOUR CODE HERE!
		
		// References: Artificial Intelligence: A Modern Approach
		// 		Page 328
		
		// function UNIFY(x, y, θ) returns a substitution to make x and y identical
		// 	inputs: x, a variable, constant, list, or compound expression
		// 			y, a variable, constant, list, or compound expression
		// 			θ, the substitution built up so far (optional, defaults to empty)
		// 	if COMPOUND?(x) and COMPOUND?(y) then
		// 		return UNIFY(x.ARGS, y.ARGS, UNIFY(x.OP, y.OP, θ))
		
		// function UNIFY-VAR(var, x, θ) returns a substitution
		// 	if {var/val} ∈ θ then return UNIFY(val, x, θ)
		// 	else if {x/val} ∈ θ then return UNIFY(var, val, θ)
		// 	else if OCCUR-CHECK?(var, x) then return failure
		// 	else return add {var/x} to θ
		
		// If the binding list is empty, return null.
		if (bl == null) {
			return null;
		}
		
		// Functions can only bind when they are the same.
		if (f1.func.equals(f2.func)) {
			// Bind the function arguments.
			return unify(f1.args, f2.args, bl);
		}

		return (null);
	}

	// unify -- Return the most general unifier for the two provided lists of
	// terms, or null if no unification is possible. The returned binding
	// list should be freshly allocated.
	public BindingList unify(List<Term> ts1, List<Term> ts2, BindingList bl) {
		if (bl == null)
			return (null);
		if ((ts1.size() == 0) && (ts2.size() == 0))
			// Empty lists match other empty lists ...
			return (new BindingList(bl));
		if ((ts1.size() == 0) || (ts2.size() == 0))
			// Ran out of arguments in one list before reaching the
			// end of the other list, which means the two argument lists
			// can't match ...
			return (null);
		List<Term> terms1 = new LinkedList<Term>();
		List<Term> terms2 = new LinkedList<Term>();
		terms1.addAll(ts1);
		terms2.addAll(ts2);
		Term t1 = terms1.get(0);
		Term t2 = terms2.get(0);
		terms1.remove(0);
		terms2.remove(0);
		return (unify(terms1, terms2, unify(t1, t2, bl)));
	}

	// askFacts -- Examine all of the facts in the knowledge base to
	// determine if any of them unify with the given literal, under the
	// given binding list. If a unification is found, return the
	// corresponding most general unifier. If none is found, return null
	// to indicate failure.
	BindingList askFacts(Literal lit, BindingList bl) {
		BindingList mgu = null; // Most General Unifier
		for (Literal fact : kb.facts) {
			mgu = unify(lit, fact, bl);
			if (mgu != null)
				return (mgu);
		}
		return (null);
	}

	// askFacts -- Examine all of the facts in the knowledge base to
	// determine if any of them unify with the given literal. If a
	// unification is found, return the corresponding most general unifier.
	// If none is found, return null to indicate failure.
	BindingList askFacts(Literal lit) {
		return (askFacts(lit, new BindingList()));
	}

	// ask -- Try to prove the given goal literal, under the constraints of
	// the given binding list, using both the list of known facts and the
	// collection of known rules. Terminate as soon as a proof is found,
	// returning the resulting binding list for that proof. Return null if
	// no proof can be found. The returned binding list should be freshly
	// allocated.
	BindingList ask(Literal goal, BindingList bl) {
		BindingList result = askFacts(goal, bl);
		if (result != null) {
			// The literal can be unified with a known fact ...
			return (result);
		}
		// Need to look at rules ...
		for (Rule candidateRule : kb.rules) {
			if (candidateRule.consequent.pred.equals(goal.pred)) {
				// The rule head uses the same predicate as the goal ...
				// Standardize apart ...
				Rule r = candidateRule.standardizeApart();
				// Check to see if the consequent unifies with the goal ...
				result = unify(goal, r.consequent, bl);
				if (result != null) {
					// This rule might be part of a proof, if we can prove
					// the rule's antecedents ...
					result = ask(r.antecedents, result);
					if (result != null) {
						// The antecedents have been proven, so the goal
						// is proven ...
						return (result);
					}
				}
			}
		}
		// No rule that matches has antecedents that can be proven. Thus,
		// the search fails ...
		return (null);
	}

	// ask -- Try to prove the given goal literal using both the list of
	// known facts and the collection of known rules. Terminate as soon as
	// a proof is found, returning the resulting binding list for that proof.
	// Return null if no proof can be found. The returned binding list
	// should be freshly allocated.
	BindingList ask(Literal goal) {
		return (ask(goal, new BindingList()));
	}

	// ask -- Try to prove the given list of goal literals, under the
	// constraints of the given binding list, using both the list of known
	// facts and the collection of known rules. Terminate as soon as a proof
	// is found, returning the resulting binding list for that proof. Return
	// null if no proof can be found. The returned binding list should be
	// freshly allocated.
	BindingList ask(List<Literal> goals, BindingList bl) {
		if (goals.size() == 0) {
			// All goals have been satisfied ...
			return (bl);
		} else {
			List<Literal> newGoals = new LinkedList<Literal>();
			newGoals.addAll(goals);
			Literal goal = newGoals.get(0);
			newGoals.remove(0);
			BindingList firstBL = ask(goal, bl);
			if (firstBL == null) {
				// Failure to prove one of the goals ...
				return (null);
			} else {
				// Try to prove the remaining goals ...
				return (ask(newGoals, firstBL));
			}
		}
	}

}

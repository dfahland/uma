# uma
Java-library for analyzing and synthesizing Petri net models using unfolding-based techniques

##PURPOSE
UMA (an Unfolding-based Model Analyzer) has been implemented for the validation of state-space construction techniques for place/transition net state spaces and scenario-based specifications. Its particular strengths include
* a compact representation of a system's state-space based on McMillan's technique of finite complete prefixes of Petri net unfoldings;
* applicable to scenario-based models with history-based enabling conditions;
* techniques to transform and change model structures based on their unfoldings.

UMA is platform independent and runs on all platforms with a Java runtime environment.

##ChangeLog
###v.1.3.0
+ filter branching processes during model simplification
+ additional option to identify implicit places using 'lpsolve'
+ additional option to identify weak implicit places much faster
+ computation of implicit places now deterministic
+ performance improvements

###v.1.2.6
+ model simplification: mapping between event log and transitions now a parameter of the simplification procedure

###v.1.2.5
* computation of implicit places now in three modes: preserve behavior, preserve visible behavior (that is recorded in a log-induced branching process), and preserve connectivity of the net wrt. the log

### v.1.2.4
* refactored code, separating branching process construction from folding routine
* fixed small bugs on folding algorithm and net synthesis

### v.1.2.3
* first changes to turn Uma into an extensible unfolding library

###v.1.2.2
* added methods for simplifying mined process models
* improved view generation
* worked on bug #16257, now ensuring that events are checked for cut-offs in the right order regardless of how the JVM orders them
* moved test cases to the JUnit framework

###v.1.2.1
* added methods for view generation
* fixed bug #16115: folding routine may result violate 1-safeness of the model
* performance improvements and bug fixes

###v.1.2.0
* added copy of Petri Net API/Java as standard interface for computing finite complete prefixes of Petri nets
* extracted unfolder sources from Greta and made a separate project for earlier version information see hub.top.GRETA.run/Changelog 

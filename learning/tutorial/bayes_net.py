
class BayesianNetwork:
	__init__(self, rep):
		self.rep = rep

	getVars(self):
		return self.rep.keys()

	getParents(self, node):
		return self.rep[node]

	pGivenParents(self, node, val):
		pass

def enumerationAsk(X, e, bn):
	q_x = {}
	for x in X:
		q_x[x] = enumerateAll(bn.getVars(), e_x + {X: x})
	return normalize(q_x)

def enumerateAll(var, e, bn):
	if len(var) == 0:
		return 1.0
	Y = var[0]
	if Y in e:
		return P_given(y, bn.getParents(Y)) * enumerateAll(var[1:], e, bn)
	else
		return sum(P_given(y, bn.getParents(Y)) * enumerateAll(var[1:], e + {Y: y}, bn))

def normalize(q):
	total = sum([q[i] for i in q])
	return {i: q[i] / total for i in q}

bn = BayesianNetwork({'D': ['B'], 'B': ['A', 'C'], 'A': [], 'C': []})
X = 'D'
e = {'A': 1, 'C': 1}

print(enumerationAsk(X, e, bn))

from keras.models import Sequential
from keras.layers import Dense
from keras.models import model_from_json
import numpy
# fix random seed for reproducibility
numpy.random.seed(7)

DIM = 12
MODELS_PATH = '/tmp/code-analyzer/models'

class Parameter:
	def __init__(self, neurons, activations, ephocs, batchSize, name=None):
		self.neurons = neurons
		self.activations = activations
		self.ephocs = ephocs
		self.batchSize = batchSize
		self.score = 0.0
		if name is None:
			name = 'tmp_name_%d' % numpy.random.randint(0, 10000)
		self.name = name

	def __str__(self):
		return "Params(neu=%s, act=%s, eph=%d, bs=%d, score=%.5f, name=%s)" % (self.neurons, self.activations, self.ephocs, self.batchSize, self.score, self.name)

	def __lt__(self, other):
		return self.score > other.score

def getParameters():
	"""
	neuronsList = [[32, 16, 8], [64, 32, 16], [24, 24, 12], [24, 24, 24], [36, 24, 12], [100, 50, 25]]
	activationsList = [['relu', 'relu', 'relu'], ['relu', 'sigmoid', 'sigmoid']]
	ephocsList = [150, 80, 120, 100, 180, 200]
	batchSizeList = [20, 25, 30, 50]
	parameters = []
	for neurons in neuronsList:
		for activations in activationsList:
			for ephocs in ephocsList:
				for batchSize in batchSizeList:
					parameters.append(Parameter(neurons, activations, ephocs, batchSize))
	"""
	parameters = [
	Parameter([128, 64, 32, 16], ['relu', 'relu', 'sigmoid', 'sigmoid'], 220, 30, '5lay_128_30'),
	Parameter([128, 64, 32, 16], ['relu', 'relu', 'sigmoid', 'sigmoid'], 220, 25, '5lay_128_25'),
	Parameter([64, 32, 16], ['relu', 'sigmoid', 'sigmoid'], 180, 50, '4lay_64_50'),
	Parameter([100, 50, 25], ['relu', 'sigmoid', 'sigmoid'], 180, 25, '4lay_100_25')
	]
	return parameters

def createMultiLayerModel(parameter):
	# create a model 85.79%
	global DIM
	model = Sequential()
	for i in range(len(parameter.neurons)):
		if i == 0:
			model.add(Dense(parameter.neurons[i], input_dim=DIM, kernel_initializer='normal', activation=parameter.activations[i]))
		else:
			model.add(Dense(parameter.neurons[i], kernel_initializer='normal', activation=parameter.activations[i]))
	model.add(Dense(1, activation='sigmoid'))
	return model

def createSimpleModel(parameter):
	# create a model. Performance: 80.27%
	model = Sequential()
	model.add(Dense(parameter.neurons[0], input_dim=12, kernel_initializer='normal', activation=parameter.activations[0]))
	model.add(Dense(1, activation='sigmoid'))
	return model

def loadDataSet(train):
	sufix = 'train' if train else 'eval-java8'
	print 'Loading', sufix, 'dataset'
	# load pima indians dataset
	dataset = numpy.loadtxt("/tmp/code-analyzer/metrics/code-metrics-codeforces-%s.csv" % sufix, delimiter=";")
	# split into input (X) and output (Y) variables
	X = dataset[:,1:]
	Y = dataset[:,0]
	print 'Train dataset loaded'
	return (X, Y)

def runTests(multiLayer, load=False):
	parameters = getParameters()
	for parameter in parameters:
		#model = createSimpleModel()
		if load:
			model = loadModel(parameter.name)
			print 'Model loaded'
		else:
			model = createMultiLayerModel(parameter) if multiLayer else createSimpleModel(parameter)
			print 'Model', 'multilayer' if multiLayer else 'simple', 'created'

			# Compile model
			model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
			print 'Model compiled'

			X, Y = loadDataSet(True)
			# Fit the model
			model.fit(X, Y, epochs=parameter.ephocs, batch_size=parameter.batchSize)
			print 'Model trained'

		X, Y = loadDataSet(False)
		# evaluate the model
		scores = model.evaluate(X, Y)
		parameter.score = scores[1]
		print parameter
		print "\nEvaluation results:\n%s: %.2f%%" % (model.metrics_names[1], scores[1]*100)
		saveModel(model, parameter.name)

	print ""
	print "******* Consolidated Results ********"
	for parameter in sorted(parameters):
		print parameter

def printPredictions(model, X):
	# calculate predictions
	predictions = model.predict(X)
	# round predictions
	rounded = [round(x[0]) for x in predictions]
	print 'Predictions:'
	print rounded

def saveModel(model, fileName):
	global MODELS_PATH
	# serialize model to JSON
	model_json = model.to_json()
	with open("%s/%s.json" % (MODELS_PATH, fileName), "w") as json_file:
		json_file.write(model_json)
	# serialize weights to HDF5
	model.save_weights("%s/%s.h5" % (MODELS_PATH, fileName))
	print("Saved model to disk")

def loadModel(fileName):
	global MODELS_PATH
	# load json and create model
	json_file = open('%s/%s.json' % (MODELS_PATH, fileName), 'r')
	loaded_model_json = json_file.read()
	json_file.close()
	loaded_model = model_from_json(loaded_model_json)
	# load weights into new model
	loaded_model.load_weights("%s/%s.h5" % (MODELS_PATH, fileName))
	print("Loaded model from disk")
	loaded_model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
	print 'Model compiled'
	return loaded_model

runTests(True, True)

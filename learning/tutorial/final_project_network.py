from keras.models import Sequential
from keras.layers import Dense
import numpy
# fix random seed for reproducibility
numpy.random.seed(7)

def createMultiLayerModel_1():
	# create a model 85.79%
	model = Sequential()
	model.add(Dense(32, input_dim=12, kernel_initializer='normal', activation='relu'))
	model.add(Dense(16, kernel_initializer='normal', activation='relu'))
	model.add(Dense(8, kernel_initializer='normal', activation='relu'))
	model.add(Dense(1, activation='sigmoid'))
	return model

def createMultiLayerModel():
	# create a model
	model = Sequential()
	model.add(Dense(15, input_dim=12, activation='relu'))
	model.add(Dense(8, activation='relu'))
	model.add(Dense(2, activation='relu'))
	model.add(Dense(1, activation='sigmoid'))
	return model

def createSimpleModel():
	# create a model. Performance: 80.27%
	model = Sequential()
	model.add(Dense(20, input_dim=12, kernel_initializer='normal', activation='relu'))
	model.add(Dense(1, activation='sigmoid'))
	return model

print 'Loading train dataset'
# load pima indians dataset
dataset_train = numpy.loadtxt("/tmp/code-analyzer/metrics/code-metrics-codeforces-train.csv", delimiter=";")
# split into input (X) and output (Y) variables
X = dataset_train[:,1:]
Y = dataset_train[:,0]
print 'Train dataset loaded'

print 'Loading evaluation dataset'
# load pima indians dataset
dataset_train = numpy.loadtxt("/tmp/code-analyzer/metrics/code-metrics-codeforces-eval.csv", delimiter=";")
# split into input (X) and output (Y) variables
X_eval = dataset_train[:,1:]
Y_eval = dataset_train[:,0]
print 'Evaluation dataset loaded'

#model = createSimpleModel()
model = createMultiLayerModel_1()
print 'Model created'

# Compile model
model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
print 'Model compiled'

# Fit the model
model.fit(X, Y, epochs=150, batch_size=20)
print 'Model trained'

# evaluate the model
scores = model.evaluate(X_eval, Y_eval)
print "\nEvaluation results:\n%s: %.2f%%" % (model.metrics_names[1], scores[1]*100)

# calculate predictions
predictions = model.predict(X_eval)
# round predictions
rounded = [round(x[0]) for x in predictions]
print 'Predictions:'
print rounded

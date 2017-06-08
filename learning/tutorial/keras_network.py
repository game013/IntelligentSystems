from keras.models import Sequential
from keras.layers import Dense
import numpy
# fix random seed for reproducibility
numpy.random.seed(7)

print 'Loading train dataset'
# load pima indians dataset
dataset_train = numpy.loadtxt("pima-indians-diabetes-train.csv", delimiter=",")
# split into input (X) and output (Y) variables
X = dataset_train[:,0:8]
Y = dataset_train[:,8]
print 'Train dataset loaded'

print 'Loading evaluation dataset'
# load pima indians dataset
dataset_train = numpy.loadtxt("pima-indians-diabetes-eval.csv", delimiter=",")
# split into input (X) and output (Y) variables
X_eval = dataset_train[:,0:8]
Y_eval = dataset_train[:,8]
print 'Evaluation dataset loaded'

# create a model
model = Sequential()
model.add(Dense(12, input_dim=8, activation='relu'))
model.add(Dense(8, activation='relu'))
model.add(Dense(1, activation='sigmoid'))
print 'Model created'

# Compile model
model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
print 'Model compiled'

# Fit the model
model.fit(X, Y, epochs=180, batch_size=15)
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

# -*- coding: utf-8 -*-


import tensorflow as tf

@tf.function
def pow(x):
  return x ** 2

# Construct a basic model.
root = tf.train.Checkpoint()
root.pow = pow

# Create the concrete function.
##since it takes only one input
#check point acts as a saving point of variable
#@tf.function is ignored by eager execution in python so it acts as function
input_data = tf.constant(1., shape=[1])
concrete_function = root.pow.get_concrete_function(input_data)

# Convert the model.
# `from_concrete_function` takes in a list of concrete functions, however,
# currently only supports converting one function at a time.
converter = tf.lite.TFLiteConverter.from_concrete_functions([concrete_function])
tflite_model = converter.convert()
open("concrete_function.tflite","wb").write(tflite_model)

# Inference Performance Optimization

The document covers several tricks of how you can tune your inference performance based on the engine you use 
including multithreading support, engine threads configuration and how to enable DNNL(MKLDNN).

## Multithreading Support

One of the advantage of Deep Java Library (DJL) is Multi-threaded inference support.
It can help to increase the throughput of your inference on multi-core CPUs and GPUs and reduce
memory consumption compare to Python.

DJL `Predictor` is not designed to be thread-safe (although some implementation is),
we recommend creating a new [Predictor](https://javadoc.io/doc/ai.djl/api/latest/ai/djl/inference/Predictor.html) for each thread.

For a reference implementation, see [Multi-threaded Benchmark](https://github.com/awslabs/djl/blob/master/examples/src/main/java/ai/djl/examples/inference/benchmark/MultithreadedBenchmark.java).

you need to set corresponding configuration based on the engine you want to use.

### Apache MXNet

#### Engine configuration
To use Apache MXNet Engine to run multi-threading, complete the following steps.

#### Enable NaiveEngine with Apache MXNet
If using the MXNet engine for a multi-threaded inference case, you need to specify the 'MXNET_ENGINE_TYPE' environment variable using the following command:

```
export MXNET_ENGINE_TYPE=NaiveEngine
```

To get the best throughput, you may also need to set 'OMP_NUM_THREADS' environment variable:

```
export OMP_NUM_THREADS=1
```

### PyTorch

### Multithreading Inference

If you use multithreading inference feature with DJL 0.8.0 and earlier version, we have to disable GC to close the NDArray by

```
# If you are using DJL 0.5.0
-Dai.djl.pytorch.disable_close_resource_on_finalize=true
# If you are using DJL 0.6.0
-Dai.djl.disable_close_resource_on_finalize=true
```

Please make sure all the NDArrays are attached to the NDManager.

### oneDNN(MKLDNN) acceleration
Unlike TensorFlow and Apache MXNet, PyTorch by default doesn't enable MKLDNN which is treated as a device type like CPU and GPU.
You can enable it by

```
-Dai.djl.pytorch.use_mkldnn=true
```

You might see the exception if certain data type or operator is not supported with the oneDNN device.

### Thread configuration
There are two configurations you can set to optimize the inference performance.

```
-Dai.djl.pytorch.num_interop_threads=[num of the interop threads]
```

It configures the number of the operations JIT interpreter fork to execute in parallel.

```
-Dai.djl.pytorch.num_threads=[num of the threads]
```

It configures the number of the threads within the operation. It is set to number of CPU cores by default.
 
You can find more detail in [PyTorch](https://pytorch.org/docs/stable/notes/cpu_threading_torchscript_inference.html).


### TensorFlow

### Multithreading Inference
You can follow the same steps as other engines for running multithreading inference using TensorFlow engine.
It's recommended to use one `Predictor` for each thread and avoid using a new `Predictor` for each inference call.
You can refer to our [Multithreading Benchmark](https://github.com/awslabs/djl/blob/master/examples/src/main/java/ai/djl/examples/inference/benchmark/MultithreadedBenchmark.java) as an example, 
here is how to run it using TensorFlow engine.

```bash
./gradlew benchmark -Dai.djl.default_engine=TensorFlow --args='-c 100 -r {"layers":"50"}'
```

### oneDNN(MKLDNN) acceleration
By default, TensorFlow engine comes with oneDNN enabled, no special configuration needed.

### Thread configuration
It's recommended to use 1 thread for operator parallelism during multithreading inference. 
You can configure it by setting the following 3 envinronment variables:

```bash
export OMP_NUM_THREADS=1
export TF_NUM_INTEROP_THREADS=1
export TF_NUM_INTRAOP_THREADS=1
```

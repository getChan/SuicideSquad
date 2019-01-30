#%%
import tensorflow as tf

#%%
#동영상 분류 클래스 갯수
NUM_CLASSES = 6

#%%
#이미지의 레이어 갯수와 이미지를 잘라낼 사이즈
#우리는 흑백이미지 이므로 CHANNEL이 하나
CROP_SIZE = 116
CHANNEL = 1

#%%
#비디오 클립당 프레임의 갯수
NUM_FRAMES_PER_CLIP = 60

#%%
#conv3d 정의
#tf.nn.bias_add = tensorflow/python/ops/nn_ops.py에 정의되어 있으며 value에 bias를 추가한다.
#tf.add의 특별한 케이스로 바이어스가 1차원으로 제한된다. 
#브로드캐스팅이 지원된다. 그래서 value는 아마도 차원들의 숫자를 가지고 있다.
#tf.add와는 같지 않게, bias의 타입은 value의 타입과 다를수 있다.
#value = 텐서의 타입 float, double, int 64, int32, uint8, int16, int8, complex64 혹은 complex128로 되어있음
#bias = 값의 마지막 차원가 일치하는 크기의 1차원 텐서

#tf.nn.conv3d = 주어진 5차원의 입력값과 필터 텐서를 3차원 컨볼루션으로 계산한다.
#input = 텐서, 반드시 half, bfloat16, float32 타입중 하나여야 하고, 형태는 [배치, 내부 깊이, 내부 높이, 내부 넓이, 내부 채널]로 구성됨 
#filter = 텐서, 반드시 같은 타입의 input을 가져야함, 형태는 [필터 깊이ㅣ, 필터 높이, 필터 넓이,내부 채널, 외부 채널]로 이루어짐. 내부 채널은 반드시 입력값과 필터의 사이에서 매치되어야 한다.
#stride = 길이가 5이상을 가진 ints의 리스트, 길이가 5인 1차원 텐서. strides[0]과 strides[4]는 무조건 1이어야 한다. input의 각 차원에 대한 슬라이딩 윈도우의 스트라이드
#padding = 모서리 부분 연산할때 어떻게 할지 SAME하고 VALID가 있음
#data_format = 데이터 포맷
#dilations = ints의 옵션 리스트
def conv3d(name, l_input, w, b):
    return tf.nn.bias_add(tf.nn.conv3d(l_input, w, stride=[1, 1, 1, 1, 1], padding='SAME'), b)

#%%
#max pooling = 입력값에 대해서 3D maxpooling을 수행함
#max pooling 이란 pixel 에서 stride를 정의해서 그 사이즈로 줄일때 최댓값을 뽑아내는 방식
#input = 텐서가 들어감, 반드시 half, bfloat16, float32 타입중 하나여야 하고, 형태는 [배치, 깊이, 열, 행, 채널]로 구성됨
#ksize = 길이가 5이상을 가진 ints의 리스트, 길이가 5인 1차원 텐서. ksize[0]과 ksize[4]는 무조건 1이어야 한다. 입력 텐서의 각 차원에 대한 창 크기. 
#stride = 길이가 5이상을 가진 ints의 리스트, 길이가 5인 1차원 텐서. strides[0]과 strides[4]는 무조건 1이어야 한다. input의 각 차원에 대한 슬라이딩 윈도우의 스트라이드
#padding = 모서리 부분 연산할때 어떻게 할지 SAME하고 VALID가 있음
#name = 오퍼레이션을 위한 이름
def max_pool(name, l_input, k):
    return tf.nn.max_pool3d(l_input, ksize=[1, k, 2, 2, 1], strides=[1, k, 2, 2, 1], padding='SAME', name=name)

#%%
#모델 구현
def inference_c3d(_X, _dropout, batch_size, _weights, _biases):
    # Convolution Layer
    conv1 = conv3d('conv1', _X, _weights['wc1'], _biases['bc1'])
    conv1 = tf.nn.relu(conv1, 'relu1')
    pool1 = max_pool('pool1', conv1, k=1)

    # Convolution Layer
    conv2 = conv3d('conv2', pool1, _weights['wc2'], _biases['bc2'])
    conv2 = tf.nn.relu(conv2, 'relu2')
    pool2 = max_pool('pool2', conv2, k=2)

    # Convolution Layer
    conv3 = conv3d('conv3a', pool2, _weights['wc3a'], _biases['bc3a'])
    conv3 = tf.nn.relu(conv3, 'relu3a')
    conv3 = conv3d('conv3b', conv3, _weights['wc3b'], _biases['bc3b'])
    conv3 = tf.nn.relu(conv3, 'relu3b')
    pool3 = max_pool('pool3', conv3, k=2)

    # Convolution Layer
    conv4 = conv3d('conv4a', pool3, _weights['wc4a'], _biases['bc4a'])
    conv4 = tf.nn.relu(conv4, 'relu4a')
    conv4 = conv3d('conv4b', conv4, _weights['wc4b'], _biases['bc4b'])
    conv4 = tf.nn.relu(conv4, 'relu4b')
    pool4 = max_pool('pool4', conv4, k=2)

    # Convolution Layer
    conv5 = conv3d('conv5a', pool4, _weights['wc5a'], _biases['bc5a'])
    conv5 = tf.nn.relu(conv5, 'relu5a')
    conv5 = conv3d('conv5b', conv5, _weights['wc5b'], _biases['bc5b'])
    conv5 = tf.nn.relu(conv5, 'relu5b')
    pool5 = max_pool('pool5', conv5, k=2)

    # Fully connected layer
    pool5 = tf.transpose(pool5, perm=[0,1,4,2,3])
    dense1 = tf.reshape(pool5, [batch_size, _weights['wd1'].get_shape().as_list()[0]]) # Reshape conv3 output to fit dense layer input
    dense1 = tf.matmul(dense1, _weights['wd1']) + _biases['bd1']

    dense1 = tf.nn.relu(dense1, name='fc1') # Relu activation
    dense1 = tf.nn.dropout(dense1, _dropout)

    dense2 = tf.nn.relu(tf.matmul(dense1, _weights['wd2']) + _biases['bd2'], name='fc2') # Relu activation
    dense2 = tf.nn.dropout(dense2, _dropout)

    #어떤 종류의 클래스인지 예측
    out = tf.matmul(dense2, _weights['out']) + _biases['out']

    return out
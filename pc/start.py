from flask import Flask
from pynput.keyboard import Key, Controller

keyboard = Controller()
app = Flask(__name__)

@app.route('/')
def start():
    return "pptContol"

@app.route('/<name>')
def sendkey(name):
    if name == 'left':
        keyboard.press(Key.left)
        keyboard.release(Key.left)
    elif name == 'right':
        keyboard.press(Key.right)
        keyboard.release(Key.right)
    elif name == 'start':
        keyboard.press(Key.f5)
        keyboard.release(Key.f5)
    elif name == 'stop':
        keyboard.press(Key.esc)
        keyboard.release(Key.esc)
    elif name == 'empty':
        keyboard.type('b')
    return name

app.run(debug=True, host="0.0.0.0")
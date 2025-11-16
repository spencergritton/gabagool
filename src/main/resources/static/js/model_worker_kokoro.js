import { env, KokoroTTS } from './kokoro.web.js';

let tts;

onmessage = async function(event) {
    try {
        const { data } = event;
        if (data.type === 'load_model') {
            await loadModel();
            postMessage({'type': 'loaded_model'});
        } else if (data.type === 'run_model') {
            const audioData = await generateSpeech(data.text, data.voice);
            console.log(audioData);
            postMessage({
                'type': 'new_audio',
                'sampling_rate': audioData.sampling_rate,
                'data': audioData.audio
            });
        }
    } catch (error) {
        console.error(error);
        postMessage({ error: error.message });
    }
};

const loadModel = async function() {
    env.allowLocalModels = true;
    env.allowRemoteModels = false;
    env.localModelPath = '/models';
    env.useBrowserCache = true;
    env.voiceDataUrl = '/models/Kokoro-82M-v1.0-ONNX/voices';

    tts = await KokoroTTS.from_pretrained('Kokoro-82M-v1.0-ONNX', {
      dtype: 'q8',
      device: 'wasm'
    });
    postMessage({
        'type': 'debug',
        'data': 'Successfully loaded model'
    });
}

const generateSpeech = async function(text, voice) {
    postMessage({
        'type': 'debug',
        'data': 'Generating speech: ' + text + ' ' + voice
    });
    try {
        const speech = await tts.generate(text, { voice: voice });
        postMessage({
            'type': 'debug',
            'data': 'Generated speech'
        });
        return speech;
    } catch (error) {
        postMessage({
            'type': 'debug',
            'data': 'Failed to generate speech',
            'error': error
        });
    }
}

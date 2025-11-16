import {
    AutoTokenizer,
    AutoProcessor,
    SpeechT5ForTextToSpeech,
    SpeechT5HifiGan,
    Tensor,
    env
} from 'https://cdn.jsdelivr.net/npm/@huggingface/transformers@3.7.6';

let tokenizer;
let processor;
let model;
let vocoder;
let speaker_embeddings;

onmessage = async function(event) {
    try {
        const { data } = event;
        if (data.type === 'load_model') {
            await loadModel();
            postMessage({'type': 'loaded_model', });
        } else if (data.type === 'run_model') {
            const audioData = await generateSpeech(data.text);
            postMessage({
                'type': 'new_audio',
                'sampling_rate': processor.feature_extractor.config.sampling_rate,
                'data': audioData
            });
        }
    } catch (error) {
        postMessage({ error: error.message });
    }
};

const loadModel = async function() {
    env.allowlocalmodels = true;
    env.localmodelpath = '/models';
    env.usebrowsercache = true;
    env.usewebworker = true;

    // Load the tokenizer and processor
    tokenizer = await AutoTokenizer.from_pretrained('speecht5_tts');
    processor = await AutoProcessor.from_pretrained('speecht5_tts');

    // Load the models
    model = await SpeechT5ForTextToSpeech.from_pretrained('speecht5_tts', { dtype: 'fp32' });  // Options: "fp32", "fp16", "q8", "q4"
    vocoder = await SpeechT5HifiGan.from_pretrained('speecht5_hifigan', { dtype: 'fp32' });  // Options: "fp32", "fp16", "q8", "q4"

    // Load speaker embeddings from URL
    const speaker_embeddings_data = new Float32Array(
        await (await fetch('/models/speaker_embeddings.bin')).arrayBuffer()
    );

    speaker_embeddings = new Tensor('float32', speaker_embeddings_data,
        [1, speaker_embeddings_data.length]
    );
}

const generateSpeech = async function(text) {
    // Run tokenization
    const { input_ids } = await tokenizer(text);

    // Generate waveform
    const { waveform } = await model.generate_speech(input_ids, speaker_embeddings, { vocoder });
    const { data } = await waveform;
    return data
}

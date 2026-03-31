import { useState } from 'react';
import { sendChatMessage } from '../api';

export default function ChatBot() {
  const [isOpen, setIsOpen] = useState(false);
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(false);

  const handleSend = async () => {
    if (!message.trim()) return;

    const userMsg = { sender: 'user', text: message };
    setMessages(prev => [...prev, userMsg]);
    setLoading(true);

    try {
      const data = await sendChatMessage(message, new Date().toISOString());
      const botMsg = { sender: 'bot', text: data.reply };
      setMessages(prev => [...prev, botMsg]);
    } catch {
      setMessages(prev => [
        ...prev,
        { sender: 'bot', text: '⚠ Failed to connect to chatbot service.' }
      ]);
    }

    setMessage('');
    setLoading(false);
  };

  return (
    <>
      {/* Floating Toggle Button */}
      <div
        className="chat-toggle-btn"
        onClick={() => setIsOpen(prev => !prev)}
      >
        {isOpen ? '✖' : '🤖'}
      </div>

      {/* Chat Window */}
      {isOpen && (
        <div className="chat-container">
          <div className="chat-header">
            Travel Assistant
          </div>

          <div className="chat-messages">
            {messages.map((m, i) => (
              <div key={i} className={`chat-bubble ${m.sender}`}>
                {m.text}
              </div>
            ))}
            {loading && (
              <div className="chat-bubble bot">Typing...</div>
            )}
          </div>

          <div className="chat-input">
            <input
              value={message}
              onChange={e => setMessage(e.target.value)}
              placeholder="Ask about routes..."
              onKeyDown={(e) => e.key === 'Enter' && handleSend()}
            />
            <button onClick={handleSend}>Send</button>
          </div>
        </div>
      )}
    </>
  );
}

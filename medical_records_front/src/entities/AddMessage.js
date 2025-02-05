import React, { useState } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthProvider';
import { useParams } from 'react-router-dom';

export function SendMessagePage() {
  const { userId } = useAuth();
  const { receiverId } = useParams(); // Extract receiver ID from URL parameter
  const [content, setContent] = useState('');

  const handleSendMessage = async (e) => {
    e.preventDefault(); // Prevent default form submission

    try {
      const messageData = {
        sender: userId,
        receiver: receiverId,
        content,
      };

      console.log(messageData);

      const response = await axios.post(`http://localhost:8082/send_message`, messageData);

      console.log(response.data);

      setContent("");
    } catch (error) {
      console.error('Error sending message:', error);
    }
  };

  return (
    <div className="container mt-4">
      <div className="card">
        <h5 className="card-header">Send Message</h5>
        <div className="card-body">
          {/* Form */}
          <div className="mb-3">
            <label htmlFor="message" className="form-label">
              Message:
            </label>
            <textarea
              className="form-control"
              id="message"
              value={content}
              onChange={(e) => setContent(e.target.value)}
            ></textarea>
          </div>
          <button className="btn btn-primary" onClick={handleSendMessage}>
            Send Message
          </button>
        </div>
      </div>
    </div>
  );
}

export default SendMessagePage;
